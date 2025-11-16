package pe.edu.utp.dwi.HBSGool.sesioncajero;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pe.edu.utp.dwi.HBSGool.auth.AuthService;
import pe.edu.utp.dwi.HBSGool.boveda.BovedaDto;
import pe.edu.utp.dwi.HBSGool.boveda.BovedaService;
import pe.edu.utp.dwi.HBSGool.cajero.CajeroEntity;
import pe.edu.utp.dwi.HBSGool.cajero.CajeroService;
import pe.edu.utp.dwi.HBSGool.cierrecajero.CierreCajeroEntity;
import pe.edu.utp.dwi.HBSGool.cierrecajero.CierreCajeroRepository;
import pe.edu.utp.dwi.HBSGool.exception.business.UserIsNotCashierException;
import pe.edu.utp.dwi.HBSGool.exception.notfound.CashierNotFoundException;
import pe.edu.utp.dwi.HBSGool.exception.business.SesionCajeroException;
import pe.edu.utp.dwi.HBSGool.exception.auth.UnauthenticatedException;
import pe.edu.utp.dwi.HBSGool.sesioncajero.dto.CreateCashierSessionRequest;
import pe.edu.utp.dwi.HBSGool.sesioncajero.dto.CurrentCashierSessionResult;
import pe.edu.utp.dwi.HBSGool.sesioncajero.dto.SesionCajeroDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SesionCajeroService {

    private final SesionCajeroRepository sesionCajeroRepository;

    private final CierreCajeroRepository cierreCajeroRepository;

    private final BovedaService bovedaService;

    private final AuthService authService;

    private final CajeroService cajeroService;

    public SesionCajeroDto createCashierSession(CreateCashierSessionRequest createCashierSessionRequest) {

        Integer currentUserId = authService.getCurrentUser()
                .orElseThrow(() -> new UnauthenticatedException("No hay ningún usuario autenticado en este momento."))
                .getUserId();

        Short cashierId = cajeroService.findByUserId(currentUserId)
                .orElseThrow()
                .getCashierId();

        Optional<SesionCajeroEntity> sesionCajero = sesionCajeroRepository
                .findFirstByCajero_CashierIdOrderByFechaAperturaDescIdSesionCajeroDesc(cashierId);

        if (sesionCajero.isEmpty()) return createSesion(createCashierSessionRequest, cashierId);

        Optional<CierreCajeroEntity> cierreCajero = cierreCajeroRepository
                .findBySesionCajeroId(sesionCajero.get().getIdSesionCajero());

        if (cierreCajero.isEmpty()) throw new SesionCajeroException("Existe una sesión que está abierta");

        return createSesion(createCashierSessionRequest, cashierId);

    }

    private SesionCajeroDto createSesion(CreateCashierSessionRequest createCashierSessionRequest, Short cashierId) {

        CajeroEntity cashier = cajeroService.findByCashierId(cashierId);

        SesionCajeroEntity entity = sesionCajeroRepository.save(
                SesionCajeroEntity.builder()
                        .cajero(cashier)
                        .montoInicial(createCashierSessionRequest.initialMoney())
                        .fechaApertura(LocalDateTime.now())
                        .build()
        );

        bovedaService.processCashMovement(
                BovedaDto.builder()
                        .tipoMovimientoBoveda("RETIRO")
                        .motivo("Apertura de caja")
                        .monto(entity.getMontoInicial())
                        .build()
        );

        return toDto(entity);
    }

    public CurrentCashierSessionResult getLastSesionByCajeroId(Short cajeroId, Integer usuarioId) {

        Short cashierId = cajeroId;

        if (cashierId == null) {

            if (usuarioId == null) {
                throw new SesionCajeroException("Este usuario no es un cajero.");
            }

            cashierId = cajeroService.findByUserId(usuarioId)
                    .orElseThrow(() -> new UserIsNotCashierException("Este usuario no es un cajero."))
                    .getCashierId();
        }

        Optional<SesionCajeroEntity> sesionCajero = sesionCajeroRepository
                .findFirstByCajero_CashierIdOrderByFechaAperturaDescIdSesionCajeroDesc(cashierId);

        // el caso donde no existe ninguna sesión de cajero para este cajero
        if (sesionCajero.isEmpty())
            return CurrentCashierSessionResult.builder()
                    .abierta(false)
                    .idSesion(null)
                    .idCierre(null)
                    .idCajero(cajeroId)
                    .fechaApertura(null)
                    .montoApertura(0.0)
                    .build();

        Optional<CierreCajeroEntity> cierreCajero = cierreCajeroRepository
                .findBySesionCajeroId(sesionCajero.get().getIdSesionCajero());

        SesionCajeroEntity sesion = sesionCajero.get();

        // Esta sesión de cajero no tiene un cierre asociado, es decir, está abierta
        if (cierreCajero.isEmpty())
            return CurrentCashierSessionResult.builder()
                    .abierta(true)
                    .montoApertura(sesion.getMontoInicial())
                    .fechaApertura(sesion.getFechaApertura())
                    .idSesion(sesion.getIdSesionCajero())
                    .idCierre(null)
                    .idCajero(cajeroId)
                    .build();

        return CurrentCashierSessionResult.builder()
                .abierta(false)
                .montoApertura(sesion.getMontoInicial())
                .fechaApertura(sesion.getFechaApertura())
                .idSesion(sesion.getIdSesionCajero())
                .idCierre(cierreCajero.get().getIdCierreCajero())
                .idCajero(cajeroId)
                .build();

    }

    private Page<SesionCajeroEntity> findFilteredSesions(
            Short idCajero,
            LocalDateTime fechaInicio,
            LocalDateTime fechaFin,
            Pageable pageable) {

        if (idCajero != null && fechaInicio != null && fechaFin != null) {
            return sesionCajeroRepository.findByCajero_CashierIdAndFechaAperturaBetween(idCajero, fechaInicio, fechaFin, pageable);
        } else if (idCajero != null && fechaInicio != null) {
            return sesionCajeroRepository.findByCajero_CashierIdAndFechaAperturaAfter(idCajero, fechaInicio, pageable);
        } else if (idCajero != null && fechaFin != null) {
            return sesionCajeroRepository.findByCajero_CashierIdAndFechaAperturaBefore(idCajero, fechaFin, pageable);
        } else if (idCajero != null) {
            return sesionCajeroRepository.findByCajero_CashierId(idCajero, pageable);
        } else if (fechaInicio != null && fechaFin != null) {
            return sesionCajeroRepository.findByFechaAperturaBetween(fechaInicio, fechaFin, pageable);
        } else if (fechaInicio != null) {
            return sesionCajeroRepository.findByFechaAperturaAfter(fechaInicio, pageable);
        } else if (fechaFin != null) {
            return sesionCajeroRepository.findByFechaAperturaBefore(fechaFin, pageable);
        } else {
            return sesionCajeroRepository.findAll(pageable);
        }
    }


    public Page<SesionCajeroDto> getAll(Pageable pageable) {
        Page<SesionCajeroEntity> page = sesionCajeroRepository.findAll(pageable);
        return page.map(this::toDto);
    }

    public SesionCajeroDto getById(Integer sessionId) {
        return sesionCajeroRepository.findById(sessionId)
                .map(this::toDto)
                .orElseThrow(() -> new CashierNotFoundException("No se encontró una sesión con esta ID."));
    }

    public Page<SesionCajeroDto> getByCashierId(Short cajeroId, Pageable pageable) {
        Page<SesionCajeroEntity> page = sesionCajeroRepository.findByCajero_CashierId(cajeroId, pageable);
        return page.map(this::toDto);
    }

    private SesionCajeroDto toDto(SesionCajeroEntity e) {
        return SesionCajeroDto.builder()
                .idSesionCajero(e.getIdSesionCajero())
                .idCajero(e.getCajero().getCashierId())
                .montoInicial(e.getMontoInicial())
                .fechaApertura(e.getFechaApertura())
                .build();
    }

    public List<SesionCajeroResumenDTO> getCashierClosureSummary(Short idCajero, LocalDateTime fechaInicio, LocalDateTime fechaFin, Pageable pageable) {
        Page<SesionCajeroEntity> sesiones = findFilteredSesions(idCajero, fechaInicio, fechaFin, pageable);

        List<SesionCajeroResumenDTO> resumen = new ArrayList<>();
        for (SesionCajeroEntity sesion : sesiones) {
            CierreCajeroEntity cierre = cierreCajeroRepository.findBySesionCajeroId(sesion.getIdSesionCajero())
                    .orElseThrow(() -> new CashierNotFoundException("Cajero con esta ID no encontrado."));
            if (cierre != null) {
                resumen.add(
                        new SesionCajeroResumenDTO(
                        sesion.getIdSesionCajero(),
                        cajeroService.toDTO(sesion.getCajero()),
                        sesion.getFechaApertura(),
                        cierre.getFecha(),
                        cierre.getMontoTeorico(),
                        cierre.getMontoReal(),
                        cierre.getMontoReal() - cierre.getMontoTeorico()
                ));
            }
        }
        return resumen;
    }

}

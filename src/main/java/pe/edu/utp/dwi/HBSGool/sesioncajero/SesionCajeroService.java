package pe.edu.utp.dwi.HBSGool.sesioncajero;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pe.edu.utp.dwi.HBSGool.auth.AuthService;
import pe.edu.utp.dwi.HBSGool.boveda.BovedaEntity;
import pe.edu.utp.dwi.HBSGool.boveda.BovedaRepositoy;
import pe.edu.utp.dwi.HBSGool.cajero.CajeroService;
import pe.edu.utp.dwi.HBSGool.cierrecajero.CierreCajeroEntity;
import pe.edu.utp.dwi.HBSGool.cierrecajero.CierreCajeroRepository;
import pe.edu.utp.dwi.HBSGool.exception.CashierNotFoundException;
import pe.edu.utp.dwi.HBSGool.exception.SesionCajeroException;
import pe.edu.utp.dwi.HBSGool.exception.UnauthenticatedException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SesionCajeroService {

    private final SesionCajeroRepository sesionCajeroRepository;

    private final CierreCajeroRepository cierreCajeroRepository;

    private final BovedaRepositoy bovedaRepositoy;

    private final AuthService authService;

    private final CajeroService cajeroService;

    public SesionCajeroDto createCashierSession(SesionCajeroDto sesionCajeroDto) {

        Integer currentUserId = authService.getCurrentUser()
                .orElseThrow(() -> new UnauthenticatedException("No hay ningún usuario autenticado en este momento."))
                .getUserId();

        Short cashierId = cajeroService.findByUserId(currentUserId)
                .orElseThrow()
                .getCashierId();

        Optional<SesionCajeroEntity> sesionCajero = sesionCajeroRepository
                .findFirstByCajeroIdOrderByFechaAperturaDescIdSesionCajeroDesc(cashierId);

        if (sesionCajero.isEmpty()) return createSesion(sesionCajeroDto, cashierId);

        Optional<CierreCajeroEntity> cierreCajero = cierreCajeroRepository
                .findBySesionCajeroId(sesionCajero.get().getIdSesionCajero());

        if (cierreCajero.isEmpty()) throw new SesionCajeroException("Existe una sesión que está abierta");

        return createSesion(sesionCajeroDto, cashierId);

    }

    private SesionCajeroDto createSesion(SesionCajeroDto sesionCajeroDto, Short cashierId) {
        SesionCajeroEntity entity = sesionCajeroRepository.save(
                SesionCajeroEntity.builder()
                        .cajeroId(cashierId)
                        .montoInicial(sesionCajeroDto.getMontoInicial())
                        .fechaApertura(sesionCajeroDto.getFechaApertura())
                        .build()
        );

        bovedaRepositoy.save(
                BovedaEntity.builder()
                        .sesionCajeroId(entity.getIdSesionCajero())
                        .tipoMovimientoBoveda("RETIRO")
                        .motivo("Apertura de caja")
                        .saldo(entity.getMontoInicial()*-1)
                        .build()
        );

        return toDto(entity);
    }

    public SesionCajeroDto getLastSesionByCajeroId(short cajeroId) {

        Optional<SesionCajeroEntity> sesionCajero = sesionCajeroRepository
                .findFirstByCajeroIdOrderByFechaAperturaDescIdSesionCajeroDesc(cajeroId);

        if (sesionCajero.isEmpty()) return null;

        Optional<CierreCajeroEntity> cierreCajero = cierreCajeroRepository
                .findBySesionCajeroId(sesionCajero.get().getIdSesionCajero());

        if (cierreCajero.isEmpty()) return toDto(sesionCajero.get());

        return null;

    }

    private Page<SesionCajeroEntity> findFilteredSesions(
            Short idCajero,
            LocalDateTime fechaInicio,
            LocalDateTime fechaFin,
            Pageable pageable) {

        if (idCajero != null && fechaInicio != null && fechaFin != null) {
            return sesionCajeroRepository.findByCajeroIdAndFechaAperturaBetween(idCajero, fechaInicio, fechaFin, pageable);
        } else if (idCajero != null && fechaInicio != null) {
            return sesionCajeroRepository.findByCajeroIdAndFechaAperturaAfter(idCajero, fechaInicio, pageable);
        } else if (idCajero != null && fechaFin != null) {
            return sesionCajeroRepository.findByCajeroIdAndFechaAperturaBefore(idCajero, fechaFin, pageable);
        } else if (idCajero != null) {
            return sesionCajeroRepository.findByCajeroId(idCajero, pageable);
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

    public Page<SesionCajeroDto> getById(Short cajeroId, Pageable pageable) {
        Page<SesionCajeroEntity> page = sesionCajeroRepository.findByCajeroId(cajeroId, pageable);
        return page.map(this::toDto);
    }

    private SesionCajeroDto toDto(SesionCajeroEntity e) {
        return SesionCajeroDto.builder()
                .idSesionCajero(e.getIdSesionCajero())
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
                resumen.add(new SesionCajeroResumenDTO(
                        sesion.getIdSesionCajero(),
                        sesion.getCajeroId(),
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

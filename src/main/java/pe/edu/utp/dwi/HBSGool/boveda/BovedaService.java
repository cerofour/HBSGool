package pe.edu.utp.dwi.HBSGool.boveda;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.utp.dwi.HBSGool.auth.AuthService;
import pe.edu.utp.dwi.HBSGool.cajero.CajeroEntity;
import pe.edu.utp.dwi.HBSGool.cajero.CajeroService;
import pe.edu.utp.dwi.HBSGool.cierrecajero.CierreCajeroEntity;
import pe.edu.utp.dwi.HBSGool.cierrecajero.CierreCajeroRepository;
import pe.edu.utp.dwi.HBSGool.exception.business.BovedaException;
import pe.edu.utp.dwi.HBSGool.exception.business.SesionCajeroException;
import pe.edu.utp.dwi.HBSGool.exception.auth.UnauthenticatedException;
import pe.edu.utp.dwi.HBSGool.exception.business.UserIsNotCashierException;
import pe.edu.utp.dwi.HBSGool.sesioncajero.SesionCajeroEntity;
import pe.edu.utp.dwi.HBSGool.sesioncajero.SesionCajeroRepository;
import pe.edu.utp.dwi.HBSGool.usuario.UsuarioEntity;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BovedaService {
    private final BovedaRepositoy repositoy;
    private final AuthService authService;
    private final CajeroService cajeroService;
    private final SesionCajeroRepository sesionCajeroRepository;
    private final CierreCajeroRepository cierreCajeroRepository;

    @Transactional
    public BovedaDto processCashMovement(BovedaDto bovedaDto) {

        UsuarioEntity currentUser = authService.getCurrentUser()
                .orElseThrow(() -> new UnauthenticatedException("No hay un usuario logueado actualmente."));

        Optional<CajeroEntity> cajero = cajeroService.findByUserId(currentUser.getUserId());

        if (cajero.isEmpty()) throw new UserIsNotCashierException("Este usuario no es un cajero");

        Optional<SesionCajeroEntity> sesionCajero = sesionCajeroRepository.findFirstByCajero_CashierIdOrderByFechaAperturaDescIdSesionCajeroDesc(cajero.get().getCashierId());

        if (sesionCajero.isEmpty()) throw new SesionCajeroException("Este cajero no tiene sesion abierta.");

        Optional<CierreCajeroEntity> cierreCajero = cierreCajeroRepository.findBySesionCajeroId(sesionCajero.get().getIdSesionCajero());

        if (cierreCajero.isPresent()) throw new SesionCajeroException("Este cajero no tiene sesion abierta.");

        if (bovedaDto.getMonto() == 0) throw new BovedaException("El monto no puede ser 0");

        if (bovedaDto.getTipoMovimientoBoveda().equals("INGRESO") && bovedaDto.getMonto() < 0
            || bovedaDto.getTipoMovimientoBoveda().equals("RETIRO") && bovedaDto.getMonto() > 0)
            bovedaDto.setMonto(bovedaDto.getMonto() * -1);

        return toDto(repositoy.save(
                BovedaEntity.builder()
                        .tipoMovimientoBoveda(bovedaDto.getTipoMovimientoBoveda())
                        .monto(bovedaDto.getMonto())
                        .motivo(bovedaDto.getMotivo())
                        .sesionCajeroId(sesionCajero.get().getIdSesionCajero())
                        .build()
                )
        );

    }

    private BovedaDto toDto(BovedaEntity entity) {
        return BovedaDto.builder()
                .idMovimientoBoveda(entity.getIdMovimientoBoveda())
                .monto(entity.getMonto())
                .motivo(entity.getMotivo())
                .tipoMovimientoBoveda(entity.getTipoMovimientoBoveda())
                .build();

    }


}

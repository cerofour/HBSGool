package pe.edu.utp.dwi.HBSGool.boveda;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.utp.dwi.HBSGool.auth.AuthService;
import pe.edu.utp.dwi.HBSGool.boveda.dto.BovedaMovementDetailedResult;
import pe.edu.utp.dwi.HBSGool.boveda.dto.BovedaMovementResult;
import pe.edu.utp.dwi.HBSGool.exception.auth.UnauthenticatedException;
import pe.edu.utp.dwi.HBSGool.exception.business.BovedaException;
import pe.edu.utp.dwi.HBSGool.usuario.UsuarioEntity;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BovedaService {
    private final BovedaRepositoy repositoy;
    private final AuthService authService;

    @Transactional
    public BovedaMovementResult processCashMovement(BovedaMovementRequest bovedaMovementRequest) {

        UsuarioEntity currentUser = authService.getCurrentUser()
                .orElseThrow(() -> new UnauthenticatedException("Para realizar esta consulta debe estar autenticado."));

        if (bovedaMovementRequest.getMonto() == 0) throw new BovedaException("El monto no puede ser 0");

        if (bovedaMovementRequest.getTipoMovimientoBoveda().equals("INGRESO") && bovedaMovementRequest.getMonto() < 0
            || bovedaMovementRequest.getTipoMovimientoBoveda().equals("RETIRO") && bovedaMovementRequest.getMonto() > 0)
            bovedaMovementRequest.setMonto(bovedaMovementRequest.getMonto() * -1);

        return toDto(repositoy.save(
                BovedaEntity.builder()
                        .tipoMovimientoBoveda(bovedaMovementRequest.getTipoMovimientoBoveda())
                        .monto(bovedaMovementRequest.getMonto())
                        .motivo(bovedaMovementRequest.getMotivo())
                        .usuario(currentUser)
                        .build()
                )
        );

    }

    private BovedaMovementResult toDto(BovedaEntity entity) {
        return BovedaMovementResult.builder()
                .idMovimiento(entity.getIdMovimientoBoveda())
                .usuarioId(entity.getUsuario().getUserId())
                .monto(entity.getMonto())
                .motivo(entity.getMotivo())
                .tipoMovimientoBoveda(entity.getTipoMovimientoBoveda())
                .build();

    }


    public BovedaMovementDetailedResult getAllMovementsDetailed() {
        List<BovedaEntity> moves = repositoy.findAll();

        BigDecimal totalDinero = new BigDecimal("0.0");
        int movimientosTotales = 0;

        for (BovedaEntity move : moves) {
            totalDinero = totalDinero.add(BigDecimal.valueOf(move.getMonto()));
            movimientosTotales++;
        }

        return BovedaMovementDetailedResult.builder()
                .dineroTotal(totalDinero)
                .movimientosTotales(movimientosTotales)
                .movimientos(
                        moves.stream().map(this::toDto).toList()
                )
                .build();
    }
}

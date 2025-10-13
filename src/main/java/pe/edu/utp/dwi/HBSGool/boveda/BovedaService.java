package pe.edu.utp.dwi.HBSGool.boveda;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BovedaService {
    private final BovedaRepositoy repositoy;

    public BovedaDto processCashMovement(BovedaDto bovedaDto) {
        return toDto(repositoy.save(
                BovedaEntity.builder()
                        .tipoMovimientoBoveda(bovedaDto.getTipoMovimientoBoveda())
                        .monto(bovedaDto.getSaldo())
                        .motivo(bovedaDto.getMotivo())
                        .sesionCajeroId(bovedaDto.getSesionCajeroId())
                        .build()
                )
        );

    }

    private BovedaDto toDto(BovedaEntity entity) {
        return BovedaDto.builder()
                .idMovimientoBoveda(entity.getIdMovimientoBoveda())
                .saldo(entity.getMonto())
                .motivo(entity.getMotivo())
                .sesionCajeroId(entity.getSesionCajeroId())
                .tipoMovimientoBoveda(entity.getTipoMovimientoBoveda())
                .build();

    }


}

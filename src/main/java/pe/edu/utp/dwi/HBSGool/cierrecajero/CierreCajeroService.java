package pe.edu.utp.dwi.HBSGool.cierrecajero;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.utp.dwi.HBSGool.boveda.BovedaEntity;
import pe.edu.utp.dwi.HBSGool.boveda.BovedaRepositoy;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CierreCajeroService {

    private final CierreCajeroRepository cierreCajeroRepository;

    private final BovedaRepositoy bovedaRepositoy;

    public CierreCajeroDto logoutCashier(CierreCajeroDto cierreCajeroDto) {

        Optional<CierreCajeroEntity> cierreCajero = cierreCajeroRepository
                .findBySesionCajeroId(cierreCajeroDto.getSesionCajeroId());

        if(cierreCajero.isPresent()) throw new RuntimeException("Ya existe un cierre cajero para dicha sesion");

        CierreCajeroEntity entity = cierreCajeroRepository.save(
                CierreCajeroEntity.builder()
                        .sesionCajeroId(cierreCajeroDto.getSesionCajeroId())
                        .fecha(cierreCajeroDto.getFecha())
                        .montoTeorico(cierreCajeroDto.getMontoTeorico())
                        .montoReal(cierreCajeroDto.getMontoReal())
                        .build()
        );

        bovedaRepositoy.save(
                BovedaEntity.builder()
                        .sesionCajeroId(cierreCajeroDto.getSesionCajeroId())
                        .tipoMovimientoBoveda("INGRESO")
                        .motivo("Cierre de caja")
                        .saldo(cierreCajeroDto.getMontoReal())
                        .build()
        );

        return toDto(entity);

    }

    private CierreCajeroDto toDto(CierreCajeroEntity e) {
        return CierreCajeroDto.builder()
                .idCierreCajero(e.getIdCierreCajero())
                .sesionCajeroId(e.getSesionCajeroId())
                .fecha(e.getFecha())
                .montoTeorico(e.getMontoTeorico())
                .montoReal(e.getMontoReal())
                .build();
    }

}

package pe.edu.utp.dwi.HBSGool.sesioncajero;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pe.edu.utp.dwi.HBSGool.boveda.BovedaEntity;
import pe.edu.utp.dwi.HBSGool.boveda.BovedaRepositoy;
import pe.edu.utp.dwi.HBSGool.cierrecajero.CierreCajeroEntity;
import pe.edu.utp.dwi.HBSGool.cierrecajero.CierreCajeroRepository;
import pe.edu.utp.dwi.HBSGool.exception.SesionCajeroException;
import pe.edu.utp.dwi.HBSGool.review.ReviewEntity;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SesionCajeroService {

    private final SesionCajeroRepository sesionCajeroRepository;

    private final CierreCajeroRepository cierreCajeroRepository;

    private final BovedaRepositoy bovedaRepositoy;

    public SesionCajeroDto createCashierSession(SesionCajeroDto sesionCajeroDto) {

        Optional<SesionCajeroEntity> sesionCajero = sesionCajeroRepository
                .findFirstByCajeroIdOrderByFechaAperturaDescIdSesionCajeroDesc(sesionCajeroDto.getCajeroId());

        if (sesionCajero.isEmpty()) return createSesion(sesionCajeroDto);

        Optional<CierreCajeroEntity> cierreCajero = cierreCajeroRepository
                .findBySesionCajeroId(sesionCajero.get().getIdSesionCajero());

        if (cierreCajero.isEmpty()) throw new SesionCajeroException("Existe una sesión que está abierta");

        return createSesion(sesionCajeroDto);

    }

    private SesionCajeroDto createSesion(SesionCajeroDto sesionCajeroDto) {
        SesionCajeroEntity entity = sesionCajeroRepository.save(
                SesionCajeroEntity.builder()
                        .cajeroId(sesionCajeroDto.getCajeroId())
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
                .cajeroId(e.getCajeroId())
                .montoInicial(e.getMontoInicial())
                .fechaApertura(e.getFechaApertura())
                .build();
    }

}

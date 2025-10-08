package pe.edu.utp.dwi.HBSGool.pago;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PagoService {

    private final PagoRepository repository;

    public Page<PagoDto> listPagos(
            Integer reservacionId,
            Integer sesionCajeroId,
            String medioPago,
            Pageable pageable
    ) {
        Page<PagoEntity> page;

        if (reservacionId != null) {
            page = repository.findByReservacionId(reservacionId, pageable);
        } else if (sesionCajeroId != null) {
            page = repository.findBySesionCajeroId(sesionCajeroId, pageable);
        } else if (medioPago != null) {
            page = repository.findByMedioPago(medioPago, pageable);
        } else {
            page = repository.findAll(pageable);
        }

        return page.map(this::toDto);
    }

    public PagoDto getById(Integer id) {
        return repository.findById(id).map(this::toDto).orElse(null);
    }

    public PagoDto cancelPago(Integer idPago) {
        PagoEntity pago = repository.findById(idPago).orElse(null);
        
        if (pago == null) {
            return null;
        }
        
        if ("Cancelado".equals(pago.getEstadoPago())) {
            throw new IllegalStateException("El pago ya está cancelado");
        }
        
        pago.setEstadoPago("Cancelado");
        PagoEntity saved = repository.save(pago);
        return toDto(saved);
    }

    private PagoDto toDto(PagoEntity e) {
        return new PagoDto(
                e.getIdPago(),
                e.getReservacionId(),
                e.getSesionCajeroId(),
                e.getCantidadDinero(),
                e.getFecha(),
                e.getMedioPago(),
                e.getEstadoPago()
        );
    }
}

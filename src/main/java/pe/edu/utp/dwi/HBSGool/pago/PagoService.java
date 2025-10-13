package pe.edu.utp.dwi.HBSGool.pago;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pe.edu.utp.dwi.HBSGool.exception.ReservationNotFoundException;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

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

    public ResponseEntity<Resource> getPaymentEvidence(Integer paymentId) {

        PagoEntity pago = repository.findById(paymentId)
                .orElseThrow(() -> new ReservationNotFoundException(
                        "Pago no encontrado con ID: " + paymentId
                ));

        String evidenciaPath = pago.getEvidencia();

        if (evidenciaPath == null || evidenciaPath.isBlank()) {
            throw new RuntimeException("No hay evidencia disponible para este pago.");
        }

        try {
            Path path = Paths.get(evidenciaPath);
            if (!Files.exists(path)) {
                throw new RuntimeException("Archivo de evidencia no encontrado en el servidor.");
            }

            Resource resource = new UrlResource(path.toUri());
            String mimeType = Files.probeContentType(path);

            if (mimeType == null) {
                mimeType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(mimeType))
                    .body(resource);

        } catch (IOException e) {
            throw new RuntimeException("Error al acceder al archivo de evidencia.", e);
        }
    }

    public PagoEntity createPayment(Integer reservationId, Integer cashierSessionId, BigDecimal money, String paymentMethod, String evidence) {
        PagoEntity entity = new PagoEntity(null,
                reservationId,
                cashierSessionId,
                money,
                LocalDateTime.now(),
                paymentMethod,
                "PENDIENTE",
                evidence);

        return repository.save(entity);
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

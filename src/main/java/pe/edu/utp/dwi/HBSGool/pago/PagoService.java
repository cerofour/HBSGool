package pe.edu.utp.dwi.HBSGool.pago;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.utp.dwi.HBSGool.cajero.CajeroEntity;
import pe.edu.utp.dwi.HBSGool.cajero.CajeroService;
import pe.edu.utp.dwi.HBSGool.exception.business.InvalidMoneyAmountException;
import pe.edu.utp.dwi.HBSGool.exception.notfound.ReservationNotFoundException;
import pe.edu.utp.dwi.HBSGool.reservacion.ReservacionEntity;
import pe.edu.utp.dwi.HBSGool.reservacion.ReservacionRepository;
import pe.edu.utp.dwi.HBSGool.shared.FileStorageService;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PagoService {

    private final PagoRepository repository;
    private final ReservacionRepository reservacionRepository;
    private final FileStorageService fileStorageService;
    private final CajeroService cashierService;

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

    public PagoEntity createPayment(Integer reservationId, Integer cashierSessionId, BigDecimal money, String paymentMethod, String evidence, String estadoPago) {

        ReservacionEntity reservation = reservacionRepository.findById(reservationId)
                .orElseThrow();

        if (money.compareTo(reservation.getPrecioTotal()) > 0)
            throw new InvalidMoneyAmountException(String.format("El pago de %.2f excede al precio de la reservación %.2f",
                    money, reservation.getPrecioTotal()));

        PagoEntity entity = new PagoEntity(null,
                reservationId,
                cashierSessionId,
                money,
                LocalDateTime.now(),
                paymentMethod,
                estadoPago,
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
        
        pago.setEstadoPago("RECHAZADO");
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
                e.getEstadoPago(),
                e.getEvidencia()
        );
    }

    public Optional<PagoEntity> findById(Integer paymentId) {
        return repository.findById(paymentId);
    }

    public void markAsConfirmed(PagoEntity payment) {
        payment.setEstadoPago("CONFIRMADO");
        repository.save(payment);
    }

    public List<PagoEntity> findByReservationId(Integer reservationId) {
        return repository.findByReservacionId(reservationId);
    }

    public void verifyIfPaymentsAreCompleted(ReservacionEntity reservation) {
        List<PagoEntity> allOtherPayments = repository.findByReservacionId(reservation.getIdReservacion());

        BigDecimal totalPaid = BigDecimal.valueOf(0.0);

        for (PagoEntity p : allOtherPayments)
            totalPaid = totalPaid.add(p.getCantidadDinero());

        BigDecimal saldo = reservation.getPrecioTotal().subtract(totalPaid);

        int cmp = totalPaid.compareTo(reservation.getPrecioTotal());

        if (cmp > 0) {
            throw new InvalidMoneyAmountException(String.format("No se puede pagar más que el precio total: Precio Total (%f), Saldo: (%f)", reservation.getPrecioTotal(), saldo));
        } if (cmp < 0) {
            reservation.setEstadoReservacion("SALDO");
            reservacionRepository.save(reservation);
        } else {
            reservation.setEstadoReservacion("CONFIRMADA");
            reservacionRepository.save(reservation);
        }
    }

    @Transactional(rollbackOn = InvalidMoneyAmountException.class)
    public PagoDto crearPago(Integer reservationId, BigDecimal cantidadDinero, String medioPago, MultipartFile evidence) throws IOException {

        CajeroEntity currentCashier = cashierService.getCurrentCashier()
                .orElseThrow();

        ReservacionEntity reservation = reservacionRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException("No se encontró la reservación con ID: " + reservationId));

        String evidenciaPath = null;

        if (evidence != null && !evidence.isEmpty()) {
            evidenciaPath = fileStorageService.saveEvidenceFile(evidence, reservationId);
        }

        PagoEntity pago = PagoEntity.builder()
                .reservacionId(reservation.getIdReservacion())
                .sesionCajeroId(null) // si en el futuro se agrega sesión de cajero
                .cantidadDinero(cantidadDinero)
                .fecha(LocalDateTime.now())
                .medioPago(medioPago)
                .estadoPago("PENDIENTE")
                .evidencia(evidenciaPath)
                .build();

        repository.save(pago);

        verifyIfPaymentsAreCompleted(reservation);

        return new PagoDto(
                pago.getIdPago(),
                pago.getReservacionId(),
                Integer.valueOf(currentCashier.getCashierId()),
                pago.getCantidadDinero(),
                pago.getFecha(),
                pago.getMedioPago(),
                pago.getEstadoPago(),
                pago.getEvidencia()
        );
    }
}

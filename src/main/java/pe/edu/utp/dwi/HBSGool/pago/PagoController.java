package pe.edu.utp.dwi.HBSGool.pago;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.utp.dwi.HBSGool.exception.ReservationNotFoundException;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
public class PagoController {

    private final PagoService service;

    @GetMapping
    public ResponseEntity<Page<PagoDto>> list(
            @RequestParam(name = "reservacionId", required = false) Integer reservacionId,
            @RequestParam(name = "sesionCajeroId", required = false) Integer sesionCajeroId,
            @RequestParam(name = "medioPago", required = false) String medioPago,
            @PageableDefault(size = 10, sort = "fecha", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<PagoDto> page = service.listPagos(reservacionId, sesionCajeroId, medioPago, pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagoDto> getById(@PathVariable Integer id) {
        PagoDto dto = service.getById(id);
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PagoDto> cancelar(@PathVariable Integer id) {
        try {
            PagoDto dto = service.cancelPago(id);
            if (dto == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(dto);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasAnyRoles('ADMIN', 'CASHIER')")
    @GetMapping("/evidencia/{paymentId}")
    public ResponseEntity<Resource> getPaymentEvidence(@PathVariable Integer paymentId) throws IOException {
        PagoEntity pago = pagoRepository.findById(paymentId)
                .orElseThrow(() -> new ReservationNotFoundException("Pago no encontrado con ID: " + paymentId));

        if (pago.getEvidencia() == null) {
            throw new RuntimeException("No hay evidencia disponible para este pago.");
        }

        Path path = Paths.get(pago.getEvidencia());
        Resource resource = new UrlResource(path.toUri());

        if (!resource.exists()) {
            throw new RuntimeException("Archivo de evidencia no encontrado.");
        }

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG) // o detecta tipo MIME dinámicamente
                .body(resource);
    }
}

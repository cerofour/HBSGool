package pe.edu.utp.dwi.HBSGool.pago;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pe.edu.utp.dwi.HBSGool.pago.dto.PagoByIdDto;
import pe.edu.utp.dwi.HBSGool.pago.dto.PagoDto;

import java.io.IOException;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
public class PagoController {

    private final PagoService service;

    @PreAuthorize("hasAnyRole('ADMIN', 'CASHIER')")
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

    @PreAuthorize("hasAnyRole('ADMIN', 'CASHIER')")
    @GetMapping("/{id}")
    public ResponseEntity<PagoByIdDto> getById(@PathVariable Integer id) {
            PagoByIdDto dto = service.getById(id);
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'CASHIER')")
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

    @PreAuthorize("hasAnyRole('ADMIN', 'CASHIER')")
    @PostMapping(value = "/reservacion/{reservationId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PagoDto> crearPago(
            @PathVariable Integer reservationId,
            @RequestParam BigDecimal cantidadDinero,
            @RequestParam String medioPago,
            @RequestParam(required = false) MultipartFile evidencia
    ) throws IOException {
        PagoDto pagoDto = service.crearPago(reservationId, cantidadDinero, medioPago, evidencia);
        return ResponseEntity.status(HttpStatus.CREATED).body(pagoDto);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'CASHIER')")
    @GetMapping("/evidencia/{paymentId}")
    public ResponseEntity<Resource> getPaymentEvidence(@PathVariable Integer paymentId) throws IOException {
        return service.getPaymentEvidence(paymentId);
    }
}

package pe.edu.utp.dwi.HBSGool.pago;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}

package pe.edu.utp.dwi.HBSGool.reservacion;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservaciones")
@RequiredArgsConstructor
public class ReservacionController {

    private final ReservacionService service;

    @GetMapping
    public ResponseEntity<Page<ReservacionDto>> list(
            @RequestParam(name = "usuarioId", required = false) Integer usuarioId,
            @RequestParam(name = "canchaId", required = false) Short canchaId,
            @RequestParam(name = "estado", required = false) String estado,
            @RequestParam(name = "dni", required = false) String dni,
            @PageableDefault(size = 10, sort = "tiempoInicio", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<ReservacionDto> page = service.listReservaciones(usuarioId, canchaId, estado, dni, pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservacionDto> getById(@PathVariable Integer id) {
        ReservacionDto dto = service.getById(id);
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }
}

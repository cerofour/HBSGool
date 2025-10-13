package pe.edu.utp.dwi.HBSGool.reservacion;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.utp.dwi.HBSGool.reservacion.dto.CreateReservationAsCashierRequest;
import pe.edu.utp.dwi.HBSGool.reservacion.dto.CreateReservationAsCashierResult;
import pe.edu.utp.dwi.HBSGool.reservacion.dto.CreateReservationAsUserRequest;
import pe.edu.utp.dwi.HBSGool.reservacion.dto.CreateReservationAsUserResult;

@RestController
@RequestMapping("/api/reservaciones")
@RequiredArgsConstructor
public class ReservacionController {

    private final ReservacionService service;

    @PreAuthorize("hasAnyRole('ADMIN', 'CASHIER')")
    @GetMapping
    public ResponseEntity<Page<ReservacionDto>> list(
            @RequestParam(name = "usuarioId", required = false) Integer usuarioId,
            @RequestParam(name = "canchaId", required = false) Integer canchaId,
            @RequestParam(name = "estado", required = false) String estado,
            @RequestParam(name = "dni", required = false) String dni,
            @PageableDefault(size = 10, sort = "tiempoInicio", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<ReservacionDto> page = service.listReservaciones(usuarioId, canchaId, estado, dni, pageable);
        return ResponseEntity.ok(page);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'CASHIER')")
    @GetMapping("/{id}")
    public ResponseEntity<ReservacionDto> getById(@PathVariable Integer id) {
        ReservacionDto dto = service.getById(id);
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<CreateReservationAsUserResult> createReservationAsUser(@RequestBody CreateReservationAsUserRequest createReservationAsUserRequest) {
        return ResponseEntity.ok(service.createReservationAsUser(createReservationAsUserRequest));
    }

    @PreAuthorize("hasRole('CASHIER')")
    @PostMapping("/cajero")
    public ResponseEntity<CreateReservationAsCashierResult> createReservationAsCashier(@RequestBody CreateReservationAsCashierRequest request) {
        return ResponseEntity.ok(null);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'CASHIER')")
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelReservation(@PathVariable("id") Integer id) {
        service.cancelReservation(id);
        return ResponseEntity.ok(null);
    }

}

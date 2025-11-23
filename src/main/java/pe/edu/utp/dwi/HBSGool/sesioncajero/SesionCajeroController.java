package pe.edu.utp.dwi.HBSGool.sesioncajero;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.utp.dwi.HBSGool.pago.PagoDto;
import pe.edu.utp.dwi.HBSGool.sesioncajero.dto.CreateCashierSessionRequest;
import pe.edu.utp.dwi.HBSGool.sesioncajero.dto.CurrentCashierSessionResult;
import pe.edu.utp.dwi.HBSGool.sesioncajero.dto.TransaccionCajeroResult;
import pe.edu.utp.dwi.HBSGool.sesioncajero.dto.SesionCajeroDto;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/sesion_cajero")
@RequiredArgsConstructor
public class SesionCajeroController {

    private final SesionCajeroService sesionCajeroService;

    @PreAuthorize("hasAnyRole('ADMIN', 'CASHIER')")
    @GetMapping("/resumen")
    public List<SesionCajeroResumenDTO> getSesionesCajero(
            @RequestParam(name = "idCajero", required=true) Short idCajero,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin,
            @PageableDefault(size = 10, sort="fechaApertura", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return sesionCajeroService.getCashierClosureSummary(idCajero, fechaInicio, fechaFin, pageable);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'CASHIER')")
    @GetMapping("/{idSesion}/transaccion")
    public Page<PagoDto> getTransactions(
            @PathVariable Integer idSesion,
            @PageableDefault(size = 10, sort="fecha", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return sesionCajeroService.listTransactions(idSesion, pageable);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'CASHIER')")
    @GetMapping
    public ResponseEntity<Page<SesionCajeroDto>> getByCashierId(
            @RequestParam(name = "cajeroId", required = false) Short cajeroId,
            @PageableDefault(size = 10, sort = "fechaApertura", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<SesionCajeroDto> page;
        if (cajeroId != null) page = sesionCajeroService.getByCashierId(cajeroId, pageable);
        else page = sesionCajeroService.getAll(pageable);
        return ResponseEntity.ok(page);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'CASHIER')")
    @GetMapping("/ultima")
    public ResponseEntity<CurrentCashierSessionResult> getLastSesionByCajeroId(
            @RequestParam(required = false) Short cajeroId,
            @RequestParam(required = false) Integer usuarioId) {
        return ResponseEntity.ok(sesionCajeroService.getLastSesionByCajeroId(cajeroId, usuarioId));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'CASHIER')")
    @PostMapping
    public ResponseEntity<SesionCajeroDto> createCashierSession(@RequestBody CreateCashierSessionRequest createCashierSessionRequest) {
        return ResponseEntity.ok(sesionCajeroService.createCashierSession(createCashierSessionRequest));
    }
}

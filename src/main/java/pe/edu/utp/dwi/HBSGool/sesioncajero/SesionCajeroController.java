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
import pe.edu.utp.dwi.HBSGool.sesioncajero.dto.CreateCashierSessionRequest;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/sesion_cajero")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class SesionCajeroController {

    private final SesionCajeroService sesionCajeroService;

    @GetMapping("/resumen")
    public List<SesionCajeroResumenDTO> getSesionesCajero(
            @RequestParam Short idCajero,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin,
            @PageableDefault(size = 10, sort="fechaApertura", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return sesionCajeroService.getCashierClosureSummary(idCajero, fechaInicio, fechaFin, pageable);
    }

    @GetMapping
    public ResponseEntity<Page<SesionCajeroDto>> getByCashierId(
            @RequestParam(name = "cajeroId", required = false) Short cajeroId,
            @PageableDefault(size = 10, sort = "fechaApertura", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<SesionCajeroDto> page;
        if (cajeroId != null) page = sesionCajeroService.getById(cajeroId, pageable);
        else page = sesionCajeroService.getAll(pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{cashierId}/ultima")
    public ResponseEntity<SesionCajeroDto> getLastSesionByCajeroId(@PathVariable short cashierId) {
        SesionCajeroDto sesionCajero = sesionCajeroService.getLastSesionByCajeroId(cashierId);
        if(sesionCajero == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(sesionCajero);
    }

    @PostMapping
    public ResponseEntity<SesionCajeroDto> createCashierSession(@RequestBody CreateCashierSessionRequest createCashierSessionRequest) {
        return ResponseEntity.ok(sesionCajeroService.createCashierSession(createCashierSessionRequest));
    }


}

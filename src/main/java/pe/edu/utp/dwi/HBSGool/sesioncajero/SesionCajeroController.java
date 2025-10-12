package pe.edu.utp.dwi.HBSGool.sesioncajero;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sesion_cajero")
@RequiredArgsConstructor
public class SesionCajeroController {

    private final SesionCajeroService sesionCajeroService;

    @GetMapping
    public ResponseEntity<Page<SesionCajeroDto>> getById(
            @RequestParam(name = "cajeroId", required = false) Short cajeroId,
            @PageableDefault(size = 10, sort = "fechaApertura", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<SesionCajeroDto> page;
        if (cajeroId != null) page = sesionCajeroService.getById(cajeroId, pageable);
        else page = sesionCajeroService.getAll(pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SesionCajeroDto> getLastSesionByCajeroId(@PathVariable short id) {
        SesionCajeroDto sesionCajero = sesionCajeroService.getLastSesionByCajeroId(id);
        if(sesionCajero == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(sesionCajero);
    }

    @PostMapping
    public ResponseEntity<SesionCajeroDto> createCashierSession(@RequestBody SesionCajeroDto sesionCajeroDto) {
        return ResponseEntity.ok(sesionCajeroService.createCashierSession(sesionCajeroDto));
    }


}

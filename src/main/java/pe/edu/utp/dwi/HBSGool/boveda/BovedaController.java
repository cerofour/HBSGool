package pe.edu.utp.dwi.HBSGool.boveda;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.utp.dwi.HBSGool.boveda.dto.BovedaMovementDetailedResult;
import pe.edu.utp.dwi.HBSGool.boveda.dto.BovedaMovementResult;

@RestController
@RequestMapping("/api/boveda")
@RequiredArgsConstructor
public class BovedaController {

    private final BovedaService bovedaService;

    @PreAuthorize("hasAnyRole('CASHIER', 'ADMIN')")
    @PostMapping
    public ResponseEntity<BovedaMovementResult> extractMoney(@RequestBody BovedaMovementRequest bovedaMovementRequest) {
        return ResponseEntity.ok(bovedaService.processCashMovement(bovedaMovementRequest));
    }

    @PreAuthorize("hasAnyRole('CASHIER', 'ADMIN')")
    @GetMapping("")
        public ResponseEntity<BovedaMovementDetailedResult> getAllMovements() {
            return ResponseEntity.ok(bovedaService.getAllMovementsDetailed());
    }

}

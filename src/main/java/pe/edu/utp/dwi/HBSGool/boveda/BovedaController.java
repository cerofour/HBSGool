package pe.edu.utp.dwi.HBSGool.boveda;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/boveda")
@RequiredArgsConstructor
public class BovedaController {

    private final BovedaService bovedaService;

    @PreAuthorize("hasRole('CASHIER')")
    @PostMapping
    public ResponseEntity<BovedaDto> extractMoney(@RequestBody BovedaDto bovedaDto) {
        return ResponseEntity.ok(bovedaService.processCashMovement(bovedaDto));
    }

}

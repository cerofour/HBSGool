package pe.edu.utp.dwi.HBSGool.cierrecajero;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cierre_cajero")
@RequiredArgsConstructor
public class CierreCajeroController {

    private final CierreCajeroService cierreCajeroService;

    @PostMapping
    public ResponseEntity<CierreCajeroDto> logoutCashier(@RequestBody CierreCajeroDto cierreCajeroDto) {

        return ResponseEntity.ok(cierreCajeroService.logoutCashier(cierreCajeroDto));

    }


}

package pe.edu.utp.dwi.HBSGool.cajero;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.utp.dwi.HBSGool.auth.dto.RegisterRequestDTO;
import pe.edu.utp.dwi.HBSGool.cajero.dto.CashierDTO;
import pe.edu.utp.dwi.HBSGool.cajero.dto.RegisterCashierResult;

import java.util.List;

@RestController
@RequestMapping("/api/cajero")
@RequiredArgsConstructor
public class CajeroController {

	private final CajeroService cashierService;

	@PreAuthorize("hasAnyRole('ADMIN', 'CASHIER')")
	@GetMapping()
	public ResponseEntity<List<CashierDTO>> getAllCashiers() {
		return ResponseEntity.ok(cashierService.findAll());
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping()
	public RegisterCashierResult createCashier(@RequestBody RegisterRequestDTO registerRequestDTO) {
		return cashierService.createCashier(registerRequestDTO);
	}

}

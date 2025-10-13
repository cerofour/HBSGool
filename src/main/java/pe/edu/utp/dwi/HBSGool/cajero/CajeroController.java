package pe.edu.utp.dwi.HBSGool.cajero;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.utp.dwi.HBSGool.auth.dto.RegisterRequestDTO;
import pe.edu.utp.dwi.HBSGool.cajero.dto.RegisterCashierResult;

@RestController
@RequestMapping("/api/cajero")
@RequiredArgsConstructor
public class CajeroController {

	private final CajeroService cashierService;

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping()
	public RegisterCashierResult createCashier(@RequestBody RegisterRequestDTO registerRequestDTO) {
		return cashierService.createCashier(registerRequestDTO);
	}

}

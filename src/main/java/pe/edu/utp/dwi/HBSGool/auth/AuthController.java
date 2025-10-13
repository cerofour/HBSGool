package pe.edu.utp.dwi.HBSGool.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.utp.dwi.HBSGool.auth.dto.LoginRequestDTO;
import pe.edu.utp.dwi.HBSGool.auth.dto.RegisterRequestDTO;
import pe.edu.utp.dwi.HBSGool.auth.dto.RegisterUserResult;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequestDTO req) {
		try {
			return ResponseEntity.ok(authService.login(req));
		} catch (Exception e) {
			return ResponseEntity.status(401).body(
					java.util.Map.of("error", e.getMessage())
			);
		}
	}

	@PostMapping("/register")
	public ResponseEntity<RegisterUserResult> register(@RequestBody RegisterRequestDTO req) {
		try {
			return ResponseEntity.ok(authService.register(req));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(
					new RegisterUserResult(false, null, null, null, null, null, null, null)
			);
		}
	}

	@GetMapping("/me")
	public ResponseEntity<?> getCurrentUser() {
		return authService.getCurrentUser()
				.<ResponseEntity<?>>map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.status(401).body(
						java.util.Map.of("error", "No hay usuario autenticado")
				));
	}
}

package pe.edu.utp.dwi.HBSGool.auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import pe.edu.utp.dwi.HBSGool.auth.dto.*;
import pe.edu.utp.dwi.HBSGool.exception.auth.UnauthenticatedException;
import pe.edu.utp.dwi.HBSGool.usuario.UsuarioEntity;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@PostMapping("/login")
	public ResponseEntity<LoginResult> login(@Valid @RequestBody LoginRequestDTO req) {
		return ResponseEntity.ok(authService.login(req));
	}

	@PostMapping("/register")
	public ResponseEntity<RegisterUserResult> register(@Valid @RequestBody RegisterRequestDTO req) {
		try {
			return ResponseEntity.ok(authService.register(req));
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(
					new RegisterUserResult(false, null, null, null, null, null, null, null)
			);
		}
	}

	@GetMapping("/me")
	public ResponseEntity<UserProfile> getCurrentUser() {
		UsuarioEntity user = authService.getCurrentUser()
				.orElseThrow(() -> new UnauthenticatedException("No hay ningún usuario autenticado."));

		return ResponseEntity.ok(UserProfile.builder()
				.idUsuario(user.getUserId())
				.nombre(user.getName())
				.apellidoPaterno(user.getFatherLastname())
				.apellidoMaterno(user.getMotherLastname())
				.rol(user.getRol().substring(5))
				.dni(user.getDni())
				.celular(user.getCellphone())
				.email(user.getEmail())
				.build());
	}
}

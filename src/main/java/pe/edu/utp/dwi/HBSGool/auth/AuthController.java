package pe.edu.utp.dwi.HBSGool.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.utp.dwi.HBSGool.usuario.UsuarioEntity;
import pe.edu.utp.dwi.HBSGool.usuario.UsuarioRepository;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
	private final AuthenticationManager authManager;
	private final JwtUtil jwtUtil;
	private final UsuarioRepository userRepo;
	private final PasswordEncoder passwordEncoder;

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequestDTO req) {
		try {
			authManager.authenticate(new UsernamePasswordAuthenticationToken(req.username(), req.password()));
		} catch (BadCredentialsException ex) {
			return ResponseEntity.status(401).body(Map.of("error", "Credenciales inválidas"));
		}

		// generar token
		var user = userRepo.findByEmail(req.username()).orElseThrow();
		String token = jwtUtil.generateToken(user.getEmail());

		return ResponseEntity.ok(Map.of("token", token));
	}

	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody RegisterRequestDTO req) {
		if (userRepo.findByEmail(req.email()).isPresent()) {
			return ResponseEntity.badRequest().body(Map.of("error", "Usuario ya existe"));
		}

		var u = new UsuarioEntity();
		u.setName(req.name());
		u.setFatherLastname(req.fatherLastname());
		u.setMotherLastname(req.motherLastname());
		u.setDni(req.dni());
		u.setCellphone(req.cellphone());
		u.setEmail(req.email());
		u.setActive(true);
		u.setPassword(passwordEncoder.encode(req.password()));
		u.setRol("ROLE_USER");

		userRepo.save(u);

		return ResponseEntity.ok(Map.of("msg", "Usuario creado"));
	}
}

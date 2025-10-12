package pe.edu.utp.dwi.HBSGool.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pe.edu.utp.dwi.HBSGool.usuario.UsuarioEntity;
import pe.edu.utp.dwi.HBSGool.usuario.UsuarioRepository;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final AuthenticationManager authManager;
	private final JwtUtil jwtUtil;
	private final UsuarioRepository userRepo;
	private final PasswordEncoder passwordEncoder;

	/**
	 * Inicia sesión y genera un token JWT.
	 */
	public Map<String, Object> login(LoginRequestDTO req) {
		try {
			authManager.authenticate(new UsernamePasswordAuthenticationToken(req.username(), req.password()));
		} catch (BadCredentialsException ex) {
			throw new BadCredentialsException("Credenciales inválidas");
		}

		var user = userRepo.findByEmail(req.username())
				.orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

		String token = jwtUtil.generateToken(user.getEmail());
		return Map.of("token", token);
	}

	/**
	 * Registra un nuevo usuario.
	 */
	public Map<String, Object> register(RegisterRequestDTO req) {
		if (userRepo.findByEmail(req.email()).isPresent()) {
			throw new IllegalArgumentException("Usuario ya existe");
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
		return Map.of("msg", "Usuario creado");
	}

	/**
	 * Obtiene el usuario actualmente autenticado (según el JWT o sesión).
	 */
	public Optional<UsuarioEntity> getCurrentUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
			return Optional.empty();
		}

		String username = auth.getName(); // normalmente el email o username
		return userRepo.findByEmail(username);
	}
}

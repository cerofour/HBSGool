package pe.edu.utp.dwi.HBSGool.auth;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pe.edu.utp.dwi.HBSGool.auth.dto.*;
import pe.edu.utp.dwi.HBSGool.usuario.UsuarioEntity;
import pe.edu.utp.dwi.HBSGool.usuario.UsuarioRepository;

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
	public LoginResult login(LoginRequestDTO req) {
		try {
			authManager.authenticate(new UsernamePasswordAuthenticationToken(req.getEmail(), req.getContrasena()));
		} catch (BadCredentialsException ex) {
			throw new BadCredentialsException("Credenciales inválidas");
		}

		var user = userRepo.findByEmail(req.getEmail())
				.orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

		if (user.getRol().equals("ROLE_CASHIER")) {

		}

		String token = jwtUtil.generateToken(user.getEmail());
		return LoginResult.builder()
				.auth(new AuthResult(token,
						jwtUtil.extractClaim(token, Claims::getExpiration)))
				.profile(UserProfile.builder()
						.idUsuario(user.getUserId())
						.nombre(user.getName())
						.apellidoPaterno(user.getFatherLastname())
						.apellidoMaterno(user.getMotherLastname())
						.rol(user.getRol().substring(5))
						.dni(user.getDni())
						.celular(user.getCellphone())
						.email(user.getEmail())
						.build())
				.build();
	}

	/**
	 * Registra un nuevo usuario.
	 */
	public RegisterUserResult register(RegisterRequestDTO req) {
		if (userRepo.findByEmail(req.getEmail()).isPresent()) {
			throw new IllegalArgumentException("Usuario ya existe");
		}

		var u = new UsuarioEntity();
		u.setName(req.getNombre());
		u.setFatherLastname(req.getApellidoPaterno());
		u.setMotherLastname(req.getApellidoMaterno());
		u.setDni(req.getDni());
		u.setCellphone(req.getCelular());
		u.setEmail(req.getEmail());
		u.setActive(true);
		u.setPassword(passwordEncoder.encode(req.getContrasena()));
		u.setRol("ROLE_USER");

		userRepo.save(u);

		RegisterUserResult result = new RegisterUserResult(
				true,
				u.getUserId(),
				u.getName(),
				u.getFatherLastname(),
				u.getMotherLastname(),
				u.getDni(),
				u.getCellphone(),
				u.getEmail()
		);

		return result;
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

	public RegisterUserResult registerCashier(RegisterRequestDTO req) {
		if (userRepo.findByEmail(req.getEmail()).isPresent()) {
			throw new IllegalArgumentException("Usuario ya existe");
		}

		var u = new UsuarioEntity();
		u.setName(req.getNombre());
		u.setFatherLastname(req.getApellidoPaterno());
		u.setMotherLastname(req.getApellidoMaterno());
		u.setDni(req.getDni());
		u.setCellphone(req.getCelular());
		u.setEmail(req.getEmail());
		u.setActive(true);
		u.setPassword(passwordEncoder.encode(req.getContrasena()));
		u.setRol("ROLE_CASHIER");

		userRepo.save(u);

		RegisterUserResult result = new RegisterUserResult(
				true,
				u.getUserId(),
				u.getName(),
				u.getFatherLastname(),
				u.getMotherLastname(),
				u.getDni(),
				u.getCellphone(),
				u.getEmail()
		);

		return result;
	}
}

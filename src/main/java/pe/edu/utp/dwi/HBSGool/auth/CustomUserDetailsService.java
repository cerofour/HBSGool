package pe.edu.utp.dwi.HBSGool.auth;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import pe.edu.utp.dwi.HBSGool.usuario.UsernameNotFoundException;
import pe.edu.utp.dwi.HBSGool.usuario.UsuarioEntity;
import pe.edu.utp.dwi.HBSGool.usuario.UsuarioRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
	private final UsuarioRepository repo;

	@Override
	@SneakyThrows
	public UserDetails loadUserByUsername(String email) {
		UsuarioEntity usuario = repo.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));

		// Create a single authority from the "rol" column
		var authorities = List.of(new SimpleGrantedAuthority(usuario.getRol()));

		return User.builder()
				.username(usuario.getEmail())
				.password(usuario.getPassword())
				.authorities(authorities)
				.disabled(!usuario.getActive())
				.build();
	}
}

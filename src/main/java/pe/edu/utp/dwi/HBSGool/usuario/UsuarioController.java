package pe.edu.utp.dwi.HBSGool.usuario;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/usuario")
@RestController
@RequiredArgsConstructor
public class UsuarioController {

	private final UsuarioRepository usuarioRepository;

	@GetMapping("/")
	public List<UsuarioEntity> findAllUsers() {
		return usuarioRepository.findAll();
	}
}

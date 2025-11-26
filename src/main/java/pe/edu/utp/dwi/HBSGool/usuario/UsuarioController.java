package pe.edu.utp.dwi.HBSGool.usuario;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.utp.dwi.HBSGool.usuario.dto.UsuarioResult;
import pe.edu.utp.dwi.HBSGool.usuario.service.UsuarioService;

import java.util.List;

@RequestMapping("/api/usuario")
@RestController
@RequiredArgsConstructor
public class UsuarioController {

	private final UsuarioService usuarioService;

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/")
	public Page<UsuarioResult> findAllUsers(
			@PageableDefault(size = 20, sort="userId", direction = Sort.Direction.DESC) Pageable pageable
	) {
		return usuarioService.findAllUsers(pageable);
	}
}

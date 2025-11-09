package pe.edu.utp.dwi.HBSGool.cancha.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.utp.dwi.HBSGool.cancha.model.CanchaEntity;
import pe.edu.utp.dwi.HBSGool.cancha.service.CanchaService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cancha")
@RequiredArgsConstructor
public class CanchaController {
	private final CanchaService canchaService;

	@GetMapping("/public")
	public List<CanchaEntity> findAll() {
		return canchaService.findAll();
	}

    @GetMapping("/public/{canchaId}")
    public Optional<CanchaEntity> findById(@PathVariable Integer canchaId) {
        return canchaService.findByCanchaId(canchaId);
    }

	@PreAuthorize("hasRole('ADMIN')")
	@org.springframework.web.bind.annotation.PatchMapping("/{id}")
	public CanchaEntity patchCancha(
			@org.springframework.web.bind.annotation.PathVariable("id") Integer id,
			@org.springframework.web.bind.annotation.RequestBody pe.edu.utp.dwi.HBSGool.cancha.model.CanchaPatchDTO dto
	) {
		return canchaService.patchCancha(id, dto);
	}

}

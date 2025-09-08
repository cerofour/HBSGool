package pe.edu.utp.dwi.HBSGool.cancha.infra.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
		import pe.edu.utp.dwi.HBSGool.cancha.application.in.CreateCanchaUseCase;
import pe.edu.utp.dwi.HBSGool.cancha.application.in.FindCanchaUseCase;
import pe.edu.utp.dwi.HBSGool.cancha.domain.Cancha;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/canchas")
@RequiredArgsConstructor
public class CanchaController {

	private final CreateCanchaUseCase createCanchaUseCase;
	private final FindCanchaUseCase findCanchaUseCase;

	// Create cancha
	@PostMapping
	public ResponseEntity<Cancha> createCancha(@RequestBody Cancha cancha) {
		Cancha created = createCanchaUseCase.createCancha(cancha);
		return ResponseEntity.ok(created);
	}

	// Find by ID
	@GetMapping("/{uuid}")
	public ResponseEntity<Cancha> findCanchaById(@PathVariable String uuid) {
		Optional<Cancha> cancha = findCanchaUseCase.findCanchaById(uuid);
		return cancha.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

	// Find all
	@GetMapping
	public ResponseEntity<List<Cancha>> findAllCanchas() {
		List<Cancha> canchas = findCanchaUseCase.findAllCancha();
		return ResponseEntity.ok(canchas);
	}
}
package pe.edu.utp.dwi.HBSGool.cancha.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.utp.dwi.HBSGool.cancha.model.CanchaEntity;
import pe.edu.utp.dwi.HBSGool.cancha.service.CanchaService;

import java.util.List;

@RestController
@RequestMapping("/api/cancha")
@RequiredArgsConstructor
public class CanchaController {

	private final CanchaService canchaService;

	@GetMapping
	public List<CanchaEntity> findAll() {
		return canchaService.findAll();
	}

}

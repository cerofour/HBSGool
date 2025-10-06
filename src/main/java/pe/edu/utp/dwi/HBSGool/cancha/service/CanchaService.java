package pe.edu.utp.dwi.HBSGool.cancha.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.utp.dwi.HBSGool.cancha.model.CanchaEntity;
import pe.edu.utp.dwi.HBSGool.cancha.repository.CanchaRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CanchaService {
	private final CanchaRepository repository;

	public List<CanchaEntity> findAll() {
		return repository.findAll();
	}
}

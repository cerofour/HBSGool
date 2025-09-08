package pe.edu.utp.dwi.HBSGool.cancha.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.utp.dwi.HBSGool.cancha.application.in.CreateCanchaUseCase;
import pe.edu.utp.dwi.HBSGool.cancha.application.in.FindCanchaUseCase;
import pe.edu.utp.dwi.HBSGool.cancha.application.out.CanchaRepositoryPort;
import pe.edu.utp.dwi.HBSGool.cancha.domain.Cancha;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CanchaService implements CreateCanchaUseCase, FindCanchaUseCase {

	private final CanchaRepositoryPort canchaRepositoryPort;

	@Override
	public Cancha createCancha(Cancha cancha) {
		return canchaRepositoryPort.save(cancha);
	}

	@Override
	public Optional<Cancha> findCanchaById(String uuid) {
		return canchaRepositoryPort.findById(UUID.fromString(uuid));
	}

	@Override
	public List<Cancha> findAllCancha() {
		return canchaRepositoryPort.findAll();
	}
}

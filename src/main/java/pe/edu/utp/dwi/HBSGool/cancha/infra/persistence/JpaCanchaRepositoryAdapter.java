package pe.edu.utp.dwi.HBSGool.cancha.infra.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import pe.edu.utp.dwi.HBSGool.cancha.application.out.CanchaRepositoryPort;
import pe.edu.utp.dwi.HBSGool.cancha.domain.Cancha;
import pe.edu.utp.dwi.HBSGool.cancha.infra.CanchaMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class JpaCanchaRepositoryAdapter implements CanchaRepositoryPort {

	private final SpringDataCanchaRepository springDataCanchaRepository;

	@Override
	public Cancha save(Cancha cancha) {

		var canchaEntity = new CanchaEntity(
				null,
				cancha.getName(),
				cancha.getDescription(),
				cancha.getWidth(),
				cancha.getLength(),
				cancha.getSynthetic(),
				cancha.getHourPrice(),
				UUID.fromString("0bf3e038-9cc3-4463-a01e-9234501739ce")
		);

		var savedCancha = springDataCanchaRepository.save(canchaEntity);

		return CanchaMapper.toDomain(savedCancha);
	}

	@Override
	public List<Cancha> findAll() {
		return springDataCanchaRepository
				.findAll()
				.stream()
				.map(CanchaMapper::toDomain)
				.toList();
	}

	@Override
	public Optional<Cancha> findById(UUID uuid) {
		return springDataCanchaRepository
				.findById(uuid)
				.map(CanchaMapper::toDomain);
	}
}

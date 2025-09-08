package pe.edu.utp.dwi.HBSGool.cancha.application.out;

import pe.edu.utp.dwi.HBSGool.cancha.domain.Cancha;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CanchaRepositoryPort {
	Cancha              save(Cancha cancha);
	List<Cancha>        findAll();
	Optional<Cancha>    findById(UUID uuid);
}

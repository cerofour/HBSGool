package pe.edu.utp.dwi.HBSGool.cancha.application.in;

import pe.edu.utp.dwi.HBSGool.cancha.domain.Cancha;

import java.util.List;
import java.util.Optional;

public interface FindCanchaUseCase {
	Optional<Cancha>    findCanchaById(String uuid);
	List<Cancha>        findAllCancha();
}

package pe.edu.utp.dwi.HBSGool.cajero;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.utp.dwi.HBSGool.exception.UserIsNotCashierException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CajeroService {
	private final CajeroRepository repo;

	/// Retorna el cajero asociado al usuario con userId.
	public Optional<CajeroEntity> findByUserId(Integer userId) {
		CajeroEntity cashier = repo.findByUsuarioId(userId)
				.orElseThrow(() -> new UserIsNotCashierException("Este usuario no es un cajero."));

		return Optional.of(cashier);
	}
}

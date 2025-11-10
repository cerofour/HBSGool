package pe.edu.utp.dwi.HBSGool.cajero;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.utp.dwi.HBSGool.auth.AuthService;
import pe.edu.utp.dwi.HBSGool.auth.dto.RegisterRequestDTO;
import pe.edu.utp.dwi.HBSGool.auth.dto.RegisterUserResult;
import pe.edu.utp.dwi.HBSGool.cajero.dto.RegisterCashierResult;
import pe.edu.utp.dwi.HBSGool.exception.auth.UnauthenticatedException;
import pe.edu.utp.dwi.HBSGool.exception.business.UserIsNotCashierException;
import pe.edu.utp.dwi.HBSGool.usuario.UsuarioEntity;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CajeroService {
	private final CajeroRepository repo;
	private final AuthService authService;

	/// Retorna el cajero asociado al usuario con userId.
	public Optional<CajeroEntity> findByUserId(Integer userId) {
		CajeroEntity cashier = repo.findByUserId(userId)
				.orElseThrow(() -> new UserIsNotCashierException("Este usuario no es un cajero."));

		return Optional.of(cashier);
	}

	public Optional<CajeroEntity> getCurrentCashier() {
		UsuarioEntity currentUser = authService.getCurrentUser()
				.orElseThrow(() -> new UnauthenticatedException("No hay ningún usuario logueado ahora mismo."));

		CajeroEntity cashier = repo.findByUserId(currentUser.getUserId())
				.orElseThrow(() -> new UserIsNotCashierException("Este usuario no es un cajero."));

		return Optional.of(cashier);
	}

	public RegisterCashierResult createCashier(RegisterRequestDTO registerRequestDTO) {

		RegisterUserResult registerUserResult = authService.registerCashier(registerRequestDTO);

		if (registerUserResult.exito()) {
			CajeroEntity entity = new CajeroEntity(null, registerUserResult.usuarioId(), true);

			repo.save(entity);

			return new RegisterCashierResult(true,
					entity.getCashierId(),
					entity.getUserId(),
					registerUserResult.nombre(),
					registerUserResult.apellidoPaterno(),
					registerUserResult.apellidoMaterno(),
					registerUserResult.dni(),
					registerUserResult.telefono(),
					registerUserResult.email());
		}

		throw new RuntimeException("No se pudo crear el cajero.");
	}

}

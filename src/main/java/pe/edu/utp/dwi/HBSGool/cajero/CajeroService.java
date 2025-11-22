package pe.edu.utp.dwi.HBSGool.cajero;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.utp.dwi.HBSGool.auth.AuthService;
import pe.edu.utp.dwi.HBSGool.auth.dto.RegisterRequestDTO;
import pe.edu.utp.dwi.HBSGool.cajero.dto.CashierDTO;
import pe.edu.utp.dwi.HBSGool.cajero.dto.RegisterCashierResult;
import pe.edu.utp.dwi.HBSGool.exception.auth.UnauthenticatedException;
import pe.edu.utp.dwi.HBSGool.exception.business.UserIsNotCashierException;
import pe.edu.utp.dwi.HBSGool.exception.notfound.CashierNotFoundException;
import pe.edu.utp.dwi.HBSGool.usuario.UsuarioEntity;
import pe.edu.utp.dwi.HBSGool.usuario.UsuarioRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CajeroService {
	private final CajeroRepository repo;
	private final UsuarioRepository userRepo;
	private final AuthService authService;

	/// Retorna el cajero asociado al usuario con userId.
	public Optional<CajeroEntity> findByUserId(Integer userId) {
		CajeroEntity cashier = repo.findByUser_UserId(userId)
				.orElseThrow(() -> new UserIsNotCashierException("Este usuario no es un cajero."));

		return Optional.of(cashier);
	}

	public List<CashierDTO> findAll() {
		// Obtenemos todos los cajeros
		return repo.findAll()
				.stream()
				.map(cajero -> {
					// Buscamos al usuario asociado
					var user = userRepo.findById(cajero.getUser().getUserId())
							.orElse(null);

					// Construimos el DTO combinando datos
					return CashierDTO.builder()
							.idCajero(Integer.valueOf(cajero.getCashierId()))
							.idUsuario(cajero.getUser().getUserId())
							.nombreCompleto(user != null ? user.getName() + " " + user.getFatherLastname() + " " + user.getMotherLastname() : null)
							.email(user != null ? user.getEmail() : null)
							.dni(user != null ? user.getDni() : null)
							.celular(user != null ? user.getCellphone() : null)
							.activo(user != null ? cajero.getActive() : null)
							.build();
				})
				.toList();
	}

	public CashierDTO toDTO(CajeroEntity cajero) {
		return CashierDTO.builder()
				.idCajero(Integer.valueOf(cajero.getCashierId()))
				.idUsuario(cajero.getUser().getUserId())
				.nombreCompleto(cajero.getUser() != null ?
						cajero.getUser().getName() + " " + cajero.getUser().getFatherLastname() + " " + cajero.getUser().getMotherLastname()
						: null)
				.email(cajero.getUser() != null ? cajero.getUser().getEmail() : null)
				.dni(cajero.getUser() != null ? cajero.getUser().getDni() : null)
				.celular(cajero.getUser() != null ? cajero.getUser().getCellphone() : null)
				.activo(cajero.getUser() != null ? cajero.getActive() : null)
				.build();
	}

	public Optional<CajeroEntity> getCurrentCashier() {
		UsuarioEntity currentUser = authService.getCurrentUser()
				.orElseThrow(() -> new UnauthenticatedException("No hay ningún usuario logueado ahora mismo."));

		CajeroEntity cashier = repo.findByUser_UserId(currentUser.getUserId())
				.orElseThrow(() -> new UserIsNotCashierException("Este usuario no es un cajero."));

		return Optional.of(cashier);
	}

	public RegisterCashierResult createCashier(RegisterRequestDTO registerRequestDTO) {

		UsuarioEntity newUser = authService.registerCashier(registerRequestDTO);

		CajeroEntity entity = new CajeroEntity(null, newUser, true);
		repo.save(entity);
		return new RegisterCashierResult(true,
				entity.getCashierId(),
				newUser.getUserId(),
				newUser.getName(),
				newUser.getFatherLastname(),
				newUser.getMotherLastname(),
				newUser.getDni(),
				newUser.getCellphone(),
				newUser.getEmail());
	}

	public CajeroEntity findByCashierId(Short cajeroId) {
		return repo.findById(cajeroId)
				.orElseThrow(() -> new CashierNotFoundException("Cajero no encontrado"));
	}
}

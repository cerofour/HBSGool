package pe.edu.utp.dwi.HBSGool.confirmacionPagoRemoto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.utp.dwi.HBSGool.auth.AuthService;
import pe.edu.utp.dwi.HBSGool.exception.NoCashierLoggedInException;
import pe.edu.utp.dwi.HBSGool.usuario.UsuarioEntity;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RemotePaymentConfirmationService {
	private final RemotePaymentConfirmationRepository repository;
	private final AuthService authService;

	public List<RemotePaymentConfirmationDTO> findAll(
			Integer cashierId,
			LocalDateTime date,
			LocalDateTime startDate,
			LocalDateTime endDate
	) {
		List<RemotePaymentConfirmationEntity> result;

		if (cashierId != null && startDate != null && endDate != null) {
			result = repository.findByCashierIdAndDateBetween(cashierId, startDate, endDate);
		} else if (cashierId != null && date != null) {
			// Si quiere filtrar por cajero y fecha exacta
			result = repository.findByCashierId(cashierId)
					.stream()
					.filter(x -> x.getDate().toLocalDate().equals(date.toLocalDate()))
					.toList();
		} else if (cashierId != null) {
			result = repository.findByCashierId(cashierId);
		} else if (startDate != null && endDate != null) {
			result = repository.findByDateBetween(startDate, endDate);
		} else if (date != null) {
			result = repository.findByDate(date);
		} else {
			result = repository.findAll();
		}

		return result.stream()
				.map(this::toDTO)
				.toList();
	}

	/// Realiza la confirmación de un pago remoto, extrae el cashierId del usuario logueado actualmente.
	public void confirmPayment(ConfirmPaymentRequest request) {
		UsuarioEntity currentlyLoggedCashier = authService.getCurrentUser()
				.orElseThrow(NoCashierLoggedInException::new);
	}

	private RemotePaymentConfirmationDTO toDTO(RemotePaymentConfirmationEntity x) {
		return new RemotePaymentConfirmationDTO(
				x.getConfirmationId(),
				x.getPaymentId(),
				x.getCashierId(),
				x.getDate(),
				x.getEvidence()
		);
	}
}

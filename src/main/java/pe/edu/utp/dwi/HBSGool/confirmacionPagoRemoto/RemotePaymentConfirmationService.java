package pe.edu.utp.dwi.HBSGool.confirmacionPagoRemoto;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.edu.utp.dwi.HBSGool.auth.AuthService;
import pe.edu.utp.dwi.HBSGool.cajero.CajeroEntity;
import pe.edu.utp.dwi.HBSGool.cajero.CajeroService;
import pe.edu.utp.dwi.HBSGool.confirmacionPagoRemoto.dto.RemotePaymentConfirmationDTO;
import pe.edu.utp.dwi.HBSGool.exception.*;
import pe.edu.utp.dwi.HBSGool.pago.PagoEntity;
import pe.edu.utp.dwi.HBSGool.pago.PagoService;
import pe.edu.utp.dwi.HBSGool.reservacion.ReservacionEntity;
import pe.edu.utp.dwi.HBSGool.reservacion.ReservacionService;
import pe.edu.utp.dwi.HBSGool.usuario.UsuarioEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RemotePaymentConfirmationService {
	private final RemotePaymentConfirmationRepository repository;
	private final AuthService authService;
	private final CajeroService cashierService;
	private final PagoService pagoService;
	private final ReservacionService reservationService;

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
	public void confirmPayment(Integer paymentId) {
		UsuarioEntity currentlyLoggedCashier = authService.getCurrentUser()
				.orElseThrow(NoCashierLoggedInException::new);

		CajeroEntity cashier = cashierService.findByUserId(currentlyLoggedCashier.getUserId())
				.orElseThrow(NoCashierLoggedInException::new);

		PagoEntity payment = pagoService.findById(paymentId)
				.orElseThrow(() -> new PaymentDoesntExistsException(paymentId));

		ReservacionEntity reservation = reservationService.findById(payment.getReservacionId())
				.orElseThrow(() -> new UsernameNotFoundException("No se encontró esta reservación."));

		if (reservation.getEstadoReservacion().equals("FINALIZADA"))
			throw new PaymentAlreadyCompletedException();

		// método de pago debe ser diferente a EFECTIVO, ya que este es un pago remoto.
		if (payment.getMedioPago().equals("EFECTIVO")) {
			throw new PaymentMethodMustBeRemote();
		}

		RemotePaymentConfirmationEntity entity = new RemotePaymentConfirmationEntity(
				null,payment.getIdPago(), Integer.valueOf(cashier.getCashierId()), LocalDateTime.now());

		repository.save(entity);

		pagoService.markAsConfirmed(payment);
		pagoService.verifyIfPaymentsAreCompleted(reservation);
	}

	private RemotePaymentConfirmationDTO toDTO(RemotePaymentConfirmationEntity x) {
		return new RemotePaymentConfirmationDTO(
				x.getConfirmationId(),
				x.getPaymentId(),
				x.getCashierId(),
				x.getDate()
		);
	}
}

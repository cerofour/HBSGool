package pe.edu.utp.dwi.HBSGool.confirmacionPagoRemoto;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Service;
import pe.edu.utp.dwi.HBSGool.auth.AuthService;
import pe.edu.utp.dwi.HBSGool.cajero.CajeroEntity;
import pe.edu.utp.dwi.HBSGool.cajero.CajeroService;
import pe.edu.utp.dwi.HBSGool.confirmacionPagoRemoto.dto.RemotePaymentConfirmationDTO;
import pe.edu.utp.dwi.HBSGool.exception.auth.NoCashierLoggedInException;
import pe.edu.utp.dwi.HBSGool.exception.business.PaymentAlreadyCompletedException;
import pe.edu.utp.dwi.HBSGool.exception.business.PaymentMethodMustBeRemote;
import pe.edu.utp.dwi.HBSGool.exception.business.SesionCajeroException;
import pe.edu.utp.dwi.HBSGool.exception.notfound.PaymentDoesntExistsException;
import pe.edu.utp.dwi.HBSGool.exception.notfound.UsernameNotFoundException;
import pe.edu.utp.dwi.HBSGool.pago.PagoEntity;
import pe.edu.utp.dwi.HBSGool.pago.PagoService;
import pe.edu.utp.dwi.HBSGool.reservacion.ReservacionEntity;
import pe.edu.utp.dwi.HBSGool.reservacion.ReservacionService;
import pe.edu.utp.dwi.HBSGool.sesioncajero.SesionCajeroService;
import pe.edu.utp.dwi.HBSGool.sesioncajero.dto.CurrentCashierSessionResult;
import pe.edu.utp.dwi.HBSGool.usuario.UsuarioEntity;

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
	private final SesionCajeroService sesionCajeroService;

	public Page<RemotePaymentConfirmationDTO> findAll(
			Integer cashierId,
			LocalDateTime date,
			LocalDateTime startDate,
			LocalDateTime endDate,
            Pageable pageable
	) {
		Page<RemotePaymentConfirmationEntity> result;

		if (cashierId != null && startDate != null && endDate != null) {
			result = repository.findByCashierIdAndDateBetween(cashierId, startDate, endDate, pageable);
		} else if (cashierId != null && date != null) {
            Page<RemotePaymentConfirmationEntity> page = repository.findByCashierId(cashierId, pageable);
			// Si quiere filtrar por cajero y fecha exacta
            List<RemotePaymentConfirmationEntity> list = page.getContent()
                    .stream()
                    .filter(x -> x.getDate().toLocalDate().equals(date.toLocalDate()))
                    .toList();
			result = new PageImpl<>(
                    list,
                    page.getPageable(),
                    list.size()
            );
		} else if (cashierId != null) {
			result = repository.findByCashierId(cashierId, pageable);
		} else if (startDate != null && endDate != null) {
			result = repository.findByDateBetween(startDate, endDate, pageable);
		} else if (date != null) {
			result = repository.findByDate(date, pageable);
		} else {
			result = repository.findAll(pageable);
		}

		return result.map(this::toDTO);
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

		CurrentCashierSessionResult currentSession = sesionCajeroService.getLastSesionByCajeroId(cashier.getCashierId(), null);

		if (reservation.getEstadoReservacion().equals("FINALIZADA"))
			throw new PaymentAlreadyCompletedException();

		if (!currentSession.getAbierta()) {
			throw new SesionCajeroException("No existe una sesión de cajero abierta.");
		}

		// método de pago debe ser diferente a EFECTIVO, ya que este es un pago remoto.
		if (payment.getMedioPago().equals("EFECTIVO")) {
			throw new PaymentMethodMustBeRemote();
		}

		RemotePaymentConfirmationEntity entity = new RemotePaymentConfirmationEntity(
				null,payment.getIdPago(), Integer.valueOf(cashier.getCashierId()), LocalDateTime.now());

		repository.save(entity);
		pagoService.markAsConfirmed(payment, currentSession.getIdSesion());
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

	public RemotePaymentConfirmationDTO getById(Integer id) {
		RemotePaymentConfirmationEntity x = repository.findById(id)
				.orElseThrow(() -> new PaymentDoesntExistsException(id));
		return new RemotePaymentConfirmationDTO(
				x.getConfirmationId(),
				x.getPaymentId(),
				x.getCashierId(),
				x.getDate()
		);
	}

    public RemotePaymentConfirmationDTO getByPaymentId(Integer paymentId) {
		RemotePaymentConfirmationEntity x = repository.findByPaymentId(paymentId)
				.orElseThrow(() -> new PaymentDoesntExistsException(paymentId));
		return new RemotePaymentConfirmationDTO(
				x.getConfirmationId(),
				x.getPaymentId(),
				x.getCashierId(),
				x.getDate()
		);
	}
}

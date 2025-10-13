package pe.edu.utp.dwi.HBSGool.exception;

public class PaymentAlreadyCompletedException extends RuntimeException {
	public PaymentAlreadyCompletedException() {
		super("Los pagos de esta reservación ya está completado.");
	}
}

package pe.edu.utp.dwi.HBSGool.exception;

public class PaymentDoesntExistsException extends RuntimeException {
	public PaymentDoesntExistsException(Integer paymentId) {
		super(String.format("El pago con ID(%d) no existe", paymentId));
	}
}

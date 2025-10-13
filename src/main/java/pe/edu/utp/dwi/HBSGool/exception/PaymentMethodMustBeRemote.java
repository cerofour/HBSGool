package pe.edu.utp.dwi.HBSGool.exception;

public class PaymentMethodMustBeRemote extends RuntimeException {
	public PaymentMethodMustBeRemote() {
		super("El medio de pago NO debe ser EFECTIVO");
	}
}

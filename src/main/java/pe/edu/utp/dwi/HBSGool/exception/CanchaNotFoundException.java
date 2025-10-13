package pe.edu.utp.dwi.HBSGool.exception;

public class CanchaNotFoundException extends RuntimeException {
	public CanchaNotFoundException(String message) {
		super(message);
	}
	public CanchaNotFoundException(Integer notFoundId) {
		super(String.format("Cancha con ID: %d no existe.", notFoundId));
	}

}

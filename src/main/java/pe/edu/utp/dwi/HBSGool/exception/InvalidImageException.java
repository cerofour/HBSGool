package pe.edu.utp.dwi.HBSGool.exception;

public class InvalidImageException extends RuntimeException {
	public InvalidImageException() {
		super("La imagen subida es inválida.");
	}
}

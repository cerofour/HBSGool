package pe.edu.utp.dwi.HBSGool.exception;

public class InvalidImageFormatException extends RuntimeException {
	public InvalidImageFormatException() {
		super("Solo se soportan imágenes en formato PNG, JPG y JPEG");
	}
}

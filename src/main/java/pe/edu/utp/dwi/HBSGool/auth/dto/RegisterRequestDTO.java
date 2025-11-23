package pe.edu.utp.dwi.HBSGool.auth.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RegisterRequestDTO {

	@NotBlank(message = "El campo 'nombre' no puede estar vacío.")
	String nombre;

	@NotBlank(message = "El campo 'apellidoMaterno' no puede estar vacío.")
	String apellidoMaterno;

	@NotBlank(message = "El campo 'apellidoPaterno' no puede estar vacío.")
	String apellidoPaterno;

	@NotBlank(message = "El campo 'dni' no puede estar vacío.")
	@Pattern(regexp = "\\d{8}", message = "El campo 'dni' debe ser una cadena de 8 números sin espacios.")
	String dni;

	@NotBlank(message = "El campo 'celular' no puede estar vacío.")
	@Pattern(regexp = "9\\d{8}", message = "El campo 'celular' debe ser una cadena de 9 números.")
	String celular;

	@NotBlank(message = "El campo 'email' no puede estar vacío.")
	@Email
	String email;

	@NotBlank(message = "El campo 'contraseña' no puede estar vacío.")
	@Size(min = 8, message = "La contraseña debe tener mínimo 8 caracteres.")
	String contrasena;
}

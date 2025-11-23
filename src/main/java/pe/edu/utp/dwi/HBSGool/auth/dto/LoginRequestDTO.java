package pe.edu.utp.dwi.HBSGool.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDTO {

	@NotBlank(message = "El correo no puede estar vacío.")
	@Email(message = "El campo email debe ser un correo.")
	String email;

	@NotBlank(message = "La contraseña no puede estar vacía.")
	String contrasena;
}

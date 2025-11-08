package pe.edu.utp.dwi.HBSGool.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserProfile {
	private String nombre;
	private String apellidoPaterno;
	private String apellidoMaterno;
	private String rol;
	private String dni;
	private String celular;
	private String email;
}

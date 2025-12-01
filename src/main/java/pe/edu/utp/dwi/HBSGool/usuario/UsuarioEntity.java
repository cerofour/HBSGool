package pe.edu.utp.dwi.HBSGool.usuario;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Usuario")
@Getter
@Setter
// @Builder
public class UsuarioEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idUsuario")
	private Integer userId;

	@Column(nullable = false, length = 40, name = "nombre")
	private String name;

	@Column(nullable = false, length = 40, name = "apellidoPaterno")
	private String fatherLastname;

	@Column(nullable = false, length = 40, name = "apellidoMaterno")
	private String motherLastname;

	@Column(unique = true, nullable = false, name = "dni")
	private String dni;

	@Pattern(regexp = "^\\d{9}$", message = "El teléfono debe tener 9 dígitos")
	@Column(unique = true, nullable = false, length = 9, name = "telefono")
	private String cellphone;

	@Email(message = "Correo electrónico inválido")
	@Column(unique = true, nullable = false, length = 255, name = "email")
	private String email;

	@Column(nullable = false, name = "activo")
	private Boolean active = true;

	@Column(nullable = false, length = 255, name = "contrasena")
	private String password;

	@Column(nullable = false, length = 16, name = "rol")
	private String rol;
}
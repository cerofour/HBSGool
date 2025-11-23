package pe.edu.utp.dwi.HBSGool.cajero;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.edu.utp.dwi.HBSGool.usuario.UsuarioEntity;

@Entity
@Table(name = "Cajero")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CajeroEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idCajero")
	private Short cashierId;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "usuarioId", referencedColumnName = "idUsuario")
	private UsuarioEntity user;

	@Column(name = "activo")
	private Boolean active;
}

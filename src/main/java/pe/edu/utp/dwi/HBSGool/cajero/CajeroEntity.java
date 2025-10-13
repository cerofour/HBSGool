package pe.edu.utp.dwi.HBSGool.cajero;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

	@Column(name = "usuarioId")
	private Integer userId;

	@Column(name = "activo")
	private Boolean active;
}

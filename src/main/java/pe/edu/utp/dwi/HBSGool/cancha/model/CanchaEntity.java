package pe.edu.utp.dwi.HBSGool.cancha.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Cancha")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CanchaEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idCancha")
	private Integer canchaId;

	@Column(name = "nombre")
	private String name;

	@Column(name = "descripcion")
	private String description;

	@Column(name = "precioHora")
	private Double hourlyPrice;

	@Column(name = "estadoCancha")
	private String canchaState;
}

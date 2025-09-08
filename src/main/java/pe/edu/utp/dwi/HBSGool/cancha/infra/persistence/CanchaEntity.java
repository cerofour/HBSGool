package pe.edu.utp.dwi.HBSGool.cancha.infra.persistence;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "cancha")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CanchaEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO) // UUID generation
	@Column(name = "id_cancha", updatable = false, nullable = false)
	private UUID id;

	@Column(name = "nombre", nullable = false, unique = true)
	private String name;

	@Column(name = "descripcion")
	private String description;

	@Column(name = "ancho", precision = 5, scale = 2)
	private BigDecimal width;

	@Column(name = "largo", precision = 5, scale = 2)
	private BigDecimal length;

	@Column(name = "es_sintetico", nullable = false)
	private Boolean synthetic;

	@Column(name = "precio_hora", precision = 10, scale = 2, nullable = false)
	private BigDecimal hourPrice;

	@Column(name = "estado_cancha_id", nullable = false)
	private UUID canchaStateId;
}
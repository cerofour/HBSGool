package pe.edu.utp.dwi.HBSGool.cancha.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cancha {
	private UUID id;
	private String name;
	private String description;
	private BigDecimal width;
	private BigDecimal length;
	private Boolean synthetic;
	private BigDecimal hourPrice;
	private UUID canchaStateId;
}

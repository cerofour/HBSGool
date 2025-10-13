package pe.edu.utp.dwi.HBSGool.confirmacionPagoRemoto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "ConfirmacionPagoRemoto")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RemotePaymentConfirmationEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idConfirmacionPagoRemoto")
	private Integer confirmationId;

	@Column(name = "pagoId")
	private Integer paymentId;

	@Column(name = "cajeroId")
	private Integer cashierId;

	@Column(name = "fecha")
	private LocalDateTime date;
}

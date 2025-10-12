package pe.edu.utp.dwi.HBSGool.confirmacionPagoRemoto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RemotePaymentConfirmationDTO {
	private Integer         remotePaymentConfirmationId;
	private Integer         paymentId;
	private Integer         cashierId;
	private LocalDateTime   date;
	private String          paymentEvidence;
}

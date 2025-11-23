package pe.edu.utp.dwi.HBSGool.sesioncajero.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class TransaccionCajeroResult {
    private Integer idPago;
    private BigDecimal montoPago;
    private String medioPago;
    private LocalDateTime fecha;
}

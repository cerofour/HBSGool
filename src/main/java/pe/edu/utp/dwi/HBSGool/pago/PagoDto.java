package pe.edu.utp.dwi.HBSGool.pago;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagoDto {

    private Integer idPago;
    private Integer reservacionId;
    private Integer sesionCajeroId;
    private BigDecimal cantidadDinero;
    private LocalDateTime fecha;
    private String medioPago;
    private String estadoPago;
}

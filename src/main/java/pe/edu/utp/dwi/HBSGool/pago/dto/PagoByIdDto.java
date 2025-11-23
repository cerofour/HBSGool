package pe.edu.utp.dwi.HBSGool.pago.dto;

import lombok.Builder;
import lombok.Getter;
import pe.edu.utp.dwi.HBSGool.sesioncajero.dto.SesionCajeroDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class PagoByIdDto {
    private Integer idPago;
    private Integer reservacionId;
    private SesionCajeroDto sesionCajero;
    private BigDecimal cantidadDinero;
    private LocalDateTime fecha;
    private String medioPago;
    private String estadoPago;
    private String evidencia;

}

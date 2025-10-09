package pe.edu.utp.dwi.HBSGool.reservacion;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservacionDto {

    private Integer idReservacion;
    private Integer usuarioId;
    private Short canchaId;
    private Short cajeroId;
    private LocalDateTime tiempoInicio;
    private String dni;
    private String duracion;  // String legible: "2 hours", "1 hour 30 minutes"
    private BigDecimal precioTotal;
    private String estadoReservacion;
}

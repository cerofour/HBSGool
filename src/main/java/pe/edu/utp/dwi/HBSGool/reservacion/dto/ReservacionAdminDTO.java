package pe.edu.utp.dwi.HBSGool.reservacion.dto;

import lombok.Builder;
import lombok.Getter;
import pe.edu.utp.dwi.HBSGool.cajero.dto.CashierDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ReservacionAdminDTO {
    private Integer idReservacion;
    private Integer usuarioId;
    private Integer canchaId;
    private CashierDTO cajero;
    private LocalDateTime tiempoInicio;
    private String dni;
    private String duracion;  // String legible: "2 hours", "1 hour 30 minutes"
    private BigDecimal precioTotal;
    private BigDecimal saldo;
    private List<Integer> pagos;
    private String estadoReservacion;
}

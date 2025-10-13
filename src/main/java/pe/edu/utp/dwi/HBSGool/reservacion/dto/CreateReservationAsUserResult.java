package pe.edu.utp.dwi.HBSGool.reservacion.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CreateReservationAsUserResult(Integer usuarioId, Integer canchaId, String dni, LocalDateTime fechaInicio, String duracion, BigDecimal precioTotal, String estado) {
}

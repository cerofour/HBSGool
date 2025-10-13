package pe.edu.utp.dwi.HBSGool.reservacion.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CreateReservationAsUserRequest(Integer canchaId, LocalDateTime tiempoInicio, String dni, String duracion, Double montoInicial, String medioPago) {
}

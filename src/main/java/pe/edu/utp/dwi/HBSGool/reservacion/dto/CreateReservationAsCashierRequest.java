package pe.edu.utp.dwi.HBSGool.reservacion.dto;

import java.time.LocalDateTime;

public record CreateReservationAsCashierRequest(Integer canchaId, LocalDateTime tiempoInicio, String dni, String duracion, Double montoInicial, String medioPago) {
}

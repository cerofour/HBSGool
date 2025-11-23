package pe.edu.utp.dwi.HBSGool.cierrecajero.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class LogoutCashierRequest {
    private int sesionCajeroId;

    private LocalDateTime fecha;

    private double montoReal;

}

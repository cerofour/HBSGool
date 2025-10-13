package pe.edu.utp.dwi.HBSGool.cierrecajero.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LogoutCashierRequest {
    private int sesionCajeroId;

    private LocalDateTime fecha;

    private double montoReal;

}

package pe.edu.utp.dwi.HBSGool.cierrecajero;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CierreCajeroDto {
    private int idCierreCajero;

    private int sesionCajeroId;

    private LocalDateTime fecha;

    private double montoTeorico;

    private double montoReal;
}


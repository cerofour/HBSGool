package pe.edu.utp.dwi.HBSGool.sesioncajero.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SesionCajeroDto {

    private Integer idSesionCajero;

    private Short idCajero;

    private double montoInicial;

    private LocalDateTime fechaApertura;


}

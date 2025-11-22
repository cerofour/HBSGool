package pe.edu.utp.dwi.HBSGool.sesioncajero;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import pe.edu.utp.dwi.HBSGool.cajero.dto.CashierDTO;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class SesionCajeroResumenDTO {
    private Integer idSesionCajero;
    private CashierDTO cajero;
    private LocalDateTime fechaApertura;
    private LocalDateTime fechaCierre;
    private Double montoTeorico;
    private Double montoReal;
    private Double diferencia;
}

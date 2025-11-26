package pe.edu.utp.dwi.HBSGool.boveda.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BovedaMovementResult {
    private Integer idMovimiento;
    private Integer usuarioId;
    private String tipoMovimientoBoveda;
    private String motivo;
    private double monto;
}

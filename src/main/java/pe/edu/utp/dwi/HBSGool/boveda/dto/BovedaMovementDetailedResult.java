package pe.edu.utp.dwi.HBSGool.boveda.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
public class BovedaMovementDetailedResult {
    private BigDecimal dineroTotal;
    private Integer movimientosTotales;

    private List<BovedaMovementResult> movimientos;
}

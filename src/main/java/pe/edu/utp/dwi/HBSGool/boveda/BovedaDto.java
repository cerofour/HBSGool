package pe.edu.utp.dwi.HBSGool.boveda;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BovedaDto {
    private int idMovimientoBoveda;
    private int sesionCajeroId;
    private String tipoMovimientoBoveda;
    private String motivo;
    private double saldo;
}

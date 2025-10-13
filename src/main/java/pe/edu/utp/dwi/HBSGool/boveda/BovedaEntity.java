package pe.edu.utp.dwi.HBSGool.boveda;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "MovimientoBoveda")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BovedaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idMovimientoBoveda;

    private int sesionCajeroId;

    private String tipoMovimientoBoveda;

    private String motivo;

    private double monto;
}

package pe.edu.utp.dwi.HBSGool.cierrecajero;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "CierreCajero")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CierreCajeroEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idCierreCajero;

    private int sesionCajeroId;

    private LocalDateTime fecha;

    private double montoTeorico;

    private double montoReal;

}

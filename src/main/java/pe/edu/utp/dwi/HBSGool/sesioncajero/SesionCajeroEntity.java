package pe.edu.utp.dwi.HBSGool.sesioncajero;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.edu.utp.dwi.HBSGool.cajero.CajeroEntity;

import java.time.LocalDateTime;

@Entity
@Table(name = "SesionCajero")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SesionCajeroEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idSesionCajero;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cajeroId", referencedColumnName = "idCajero", nullable = false)
    private CajeroEntity cajero;

    private Double montoInicial;

    private LocalDateTime fechaApertura;

}

package pe.edu.utp.dwi.HBSGool.cancha.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "SesionCajero")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SesionCajeroEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idSesionCajero")
    private Integer idSesionCajero;

    @Column(name = "cajeroId")
    private Integer cajeroId;

    @Column(name = "fechaApertura")
    private LocalDateTime fechaApertura;

    @Column(name = "montoInicial")
    private Double montoInicial;
}

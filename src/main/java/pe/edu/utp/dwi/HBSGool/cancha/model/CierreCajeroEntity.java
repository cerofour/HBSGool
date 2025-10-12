package pe.edu.utp.dwi.HBSGool.cancha.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "CierreCajero")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CierreCajeroEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idCierreCajero")
    private Integer idCierreCajero;

    @Column(name = "sesionCajeroId")
    private Integer sesionCajeroId;

    @Column(name = "fecha")
    private LocalDateTime fecha;

    @Column(name = "montoTeorico")
    private Double montoTeorico;

    @Column(name = "montoReal")
    private Double montoReal;
}

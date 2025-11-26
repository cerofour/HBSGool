package pe.edu.utp.dwi.HBSGool.boveda;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.edu.utp.dwi.HBSGool.usuario.UsuarioEntity;

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

    @ManyToOne
    @JoinColumn(name = "usuarioid")
    private UsuarioEntity usuario;

    private String tipoMovimientoBoveda;

    private String motivo;

    private double monto;
}

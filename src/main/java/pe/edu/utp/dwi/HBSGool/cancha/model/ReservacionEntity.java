package pe.edu.utp.dwi.HBSGool.cancha.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Reservacion")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReservacionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idReservacion")
    private Integer idReservacion;

    @Column(name = "estadoReservacion")
    private String estadoReservacion;

    // Otros campos según tu modelo
}

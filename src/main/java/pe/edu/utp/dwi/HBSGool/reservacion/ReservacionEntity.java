package pe.edu.utp.dwi.HBSGool.reservacion;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Table(name = "Reservacion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservacionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idReservacion;

    private Integer usuarioId;

    private Short canchaId;

    private Short cajeroId;

    private LocalDateTime tiempoInicio;

    private String dni;

    @Column(columnDefinition = "INTERVAL")
    private Duration duracion;

    private BigDecimal precioTotal;

    private String estadoReservacion;
}

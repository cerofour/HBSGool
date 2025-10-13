package pe.edu.utp.dwi.HBSGool.reservacion;

import com.vladmihalcea.hibernate.type.interval.PostgreSQLIntervalType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Table(name = "Reservacion")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservacionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idReservacion;

    private Integer usuarioId;

    private Integer canchaId;

    private Short cajeroId;

    private LocalDateTime tiempoInicio;

    private String dni;

    @Column(columnDefinition = "INTERVAL")
    @Type(PostgreSQLIntervalType.class)
    private Duration duracion;

    private BigDecimal precioTotal;

    private String estadoReservacion;
}

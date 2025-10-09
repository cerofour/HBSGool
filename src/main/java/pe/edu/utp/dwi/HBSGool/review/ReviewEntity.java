package pe.edu.utp.dwi.HBSGool.review;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "Review")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idReview")
    private Integer idReview;

    @Column(name = "usuarioId", nullable = false)
    private Integer usuarioId;

    @Column(name = "rating", nullable = false, length = 1)
    private String rating;

    @Column(name = "comentario", nullable = false, length = 255)
    private String comentario;

    @Column(name = "creado", nullable = false)
    private LocalDateTime creado;

}

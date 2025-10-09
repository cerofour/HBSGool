package pe.edu.utp.dwi.HBSGool.review;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {
    private Integer idReview;
    private Integer usuarioId;
    private String rating;
    private String comentario;
    private LocalDateTime creado;
}

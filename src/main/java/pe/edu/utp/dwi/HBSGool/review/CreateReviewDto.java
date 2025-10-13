package pe.edu.utp.dwi.HBSGool.review;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateReviewDto {
    @NotBlank(message = "El rating es obligatorio")
    @Pattern(regexp = "[1-5]", message = "El rating debe ser entre 1 y 5")
    private String rating;
    
    @NotBlank(message = "El comentario es obligatorio")
    @Size(max = 255, message = "El comentario no puede exceder 255 caracteres")
    private String comentario;
}

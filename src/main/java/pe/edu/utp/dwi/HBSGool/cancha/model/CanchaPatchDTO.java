package pe.edu.utp.dwi.HBSGool.cancha.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CanchaPatchDTO {
    private String nombre;
    private String descripcion;
    private Double precioHora;
    private String estadoCancha;
}

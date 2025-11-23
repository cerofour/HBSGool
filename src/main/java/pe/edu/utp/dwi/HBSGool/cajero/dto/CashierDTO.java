package pe.edu.utp.dwi.HBSGool.cajero.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CashierDTO {
    private Integer idCajero;
    private Integer idUsuario;
    private String nombreCompleto;
    private String email;
    private String dni;
    private String celular;
    private Boolean activo;
}

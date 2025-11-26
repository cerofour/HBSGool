package pe.edu.utp.dwi.HBSGool.usuario.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UsuarioResult {
    private Integer idUsuario;
    private String nombreCompleto;
    private String documento;
    private String celular;
    private String correo;
    private Boolean activo;
    private String rol;
}

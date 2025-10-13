package pe.edu.utp.dwi.HBSGool.auth.dto;

public record RegisterUserResult(Boolean exito, Integer usuarioId, String nombre, String apellidoPaterno, String apellidoMaterno, String dni, String telefono, String email) {
}

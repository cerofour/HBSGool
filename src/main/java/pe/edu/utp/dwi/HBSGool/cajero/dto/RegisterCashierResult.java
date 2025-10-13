package pe.edu.utp.dwi.HBSGool.cajero.dto;

public record RegisterCashierResult(Boolean exito, Short cajeroId, Integer usuarioId, String nombre, String apellidoPaterno, String apellidoMaterno, String dni, String telefono, String email) {
}

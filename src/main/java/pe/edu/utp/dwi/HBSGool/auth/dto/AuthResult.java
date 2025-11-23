package pe.edu.utp.dwi.HBSGool.auth.dto;

import java.util.Date;

public record AuthResult(String jwtToken, Date expiracion) {
}

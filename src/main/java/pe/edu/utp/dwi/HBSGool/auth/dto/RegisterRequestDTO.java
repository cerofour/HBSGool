package pe.edu.utp.dwi.HBSGool.auth.dto;

public record RegisterRequestDTO(String name, String motherLastname, String fatherLastname, String dni, String cellphone, String email, String password) {
}

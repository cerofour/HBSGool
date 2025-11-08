package pe.edu.utp.dwi.HBSGool.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LoginResult {
	AuthResult auth;
	UserProfile profile;
}

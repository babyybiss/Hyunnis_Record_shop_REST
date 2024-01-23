package hyunni.rest.auth.dto;

import hyunni.rest.auth.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenDTO {
    private String email;
    private String userName;
    private UserRole userRole;
    private String grantType;
    private String accessToken;
    private Long tokenExpiresIn;
    private String firstLogin;
    private String message;
}

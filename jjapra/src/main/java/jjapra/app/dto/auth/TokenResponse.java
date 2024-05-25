package jjapra.app.dto.auth;

import lombok.Data;

@Data
public class TokenResponse {
    private String accessToken;

    public TokenResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}

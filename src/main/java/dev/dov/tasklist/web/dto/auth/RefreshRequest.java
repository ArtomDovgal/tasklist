package dev.dov.tasklist.web.dto.auth;

import lombok.Data;

@Data
public class RefreshRequest {

    private String refreshToken;
}

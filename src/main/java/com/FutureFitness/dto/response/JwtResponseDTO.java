package com.FutureFitness.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponseDTO {
    private String accessToken;
    private String tokenType = "Bearer";

    public JwtResponseDTO(String accessToken) {
        this.accessToken = accessToken;
    }
}

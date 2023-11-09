package com.example.capstoneproject.Dto.responses;

import com.example.capstoneproject.Dto.UserViewLoginDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

    @JsonProperty("access_token")
    private String accessToken;

    private UserViewLoginDto user;
}

package com.groupe3.pharmaconnect.security.jwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    private String secret;
    private Long expiration;
    private String issuer;
    private String tokenPrefix = "Bearer ";
    private String headerString = "Authorization";
}
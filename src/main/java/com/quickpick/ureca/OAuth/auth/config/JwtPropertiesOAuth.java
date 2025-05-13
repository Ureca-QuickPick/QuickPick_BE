package com.quickpick.ureca.OAuth.auth.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "jwt")
public class JwtPropertiesOAuth {
    private String issuer;
    private String secretKey;
}

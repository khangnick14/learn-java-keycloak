package com.nick.ms_nick_keycloak.jwt;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Setter
@Getter
@Validated
@ConfigurationProperties(prefix = "jwt.auth.converter")
public class JwtConverterProperties {

    @NotEmpty
    private String resourceId;

    @NotEmpty
    private String principalAttribute;

}

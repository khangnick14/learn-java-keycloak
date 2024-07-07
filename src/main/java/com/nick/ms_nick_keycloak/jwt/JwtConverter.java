package com.nick.ms_nick_keycloak.jwt;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class JwtConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
    private final JwtConverterProperties jwtConverterProperties;

    public JwtConverter(JwtConverterProperties jwtConverterProperties) {
        this.jwtConverterProperties = jwtConverterProperties;
    }

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Collection<GrantedAuthority> authorityCollection = Stream.concat(
                jwtGrantedAuthoritiesConverter.convert(jwt).stream(),
                extractResourceRoles(jwt).stream()).collect(Collectors.toSet());
        return new JwtAuthenticationToken(jwt, authorityCollection, getPrincipalClaimName(jwt));
    }

    private String getPrincipalClaimName(Jwt jwt) {
        String claimName;
        if (jwtConverterProperties.getPrincipalAttribute() != null) {
            claimName = jwtConverterProperties.getPrincipalAttribute();
        } else {
            claimName = JwtClaimNames.SUB;
        }
        return jwt.getClaim(claimName);
    }

    // Extract roles from JWT token claim, then convert to collection of GrantedAuthority objects
    private Collection<? extends GrantedAuthority> extractResourceRoles(Jwt jwt) {
        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");

        if (resourceAccess == null) {
            return Collections.emptySet();
        }

        if (!(resourceAccess.get(jwtConverterProperties.getResourceId())
                instanceof Map<?, ?> resource)) {
            return Collections.emptySet();
        }

        if (!(resource.get("roles") instanceof
            Collection<?> resourceRoles)) {
            return Collections.emptySet();
        }

        return resourceRoles.stream()
                .filter(role -> role instanceof String)
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toSet());
    }
}

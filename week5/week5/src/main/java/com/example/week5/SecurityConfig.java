package com.example.week5;
import java.util.*;
import java.util.stream.*;
import org.springframework.security.config.Customizer;

import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.*;
import org.springframework.security.core.authority.*;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.*;
import org.springframework.security.oauth2.core.oidc.*;
import org.springframework.security.oauth2.core.oidc.user.*;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.logout.*;

import org.springframework.core.convert.converter.Converter;
@Configuration
public class SecurityConfig {

    interface AuthoritiesConverter extends Converter<Map<String, Object>, Collection<GrantedAuthority>> {}

    @Bean
    AuthoritiesConverter realmRolesAuthoritiesConverter() {
        return claims -> {
            var realmAccess = Optional.ofNullable((Map<String, Object>) claims.get("realm_access"));
            var roles = realmAccess.flatMap(map -> Optional.ofNullable((List<String>) map.get("roles")));
            return roles.map(List::stream)
                .orElse(Stream.empty())
                .map(SimpleGrantedAuthority::new)
                .map(GrantedAuthority.class::cast)
                .toList();
        };
    }

    @Bean
    GrantedAuthoritiesMapper authenticationConverter(AuthoritiesConverter authoritiesConverter) {
        return (authorities) -> authorities.stream()
            .filter(authority -> authority instanceof OidcUserAuthority)
            .map(OidcUserAuthority.class::cast)
            .map(OidcUserAuthority::getIdToken)
            .map(OidcIdToken::getClaims)
            .map(authoritiesConverter::convert)
            .flatMap(Collection::stream)
            .collect(Collectors.toSet());
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http,
                                            ClientRegistrationRepository clientRegistrationRepository)
            throws Exception {
        http.oauth2Login(Customizer.withDefaults());

        http.logout(logout -> {
            var handler = new OidcClientInitiatedLogoutSuccessHandler(clientRegistrationRepository);
            handler.setPostLogoutRedirectUri("{baseUrl}/");
            logout.logoutSuccessHandler(handler);
        });

        http.authorizeHttpRequests(auth -> auth
            .requestMatchers("/", "/favicon.ico").permitAll()
            .requestMatchers("/nice").hasAuthority("NICE")
            .anyRequest().denyAll());

        return http.build();
    }
}
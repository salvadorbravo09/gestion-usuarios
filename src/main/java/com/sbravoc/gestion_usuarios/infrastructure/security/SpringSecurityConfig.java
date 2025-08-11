package com.sbravoc.gestion_usuarios.infrastructure.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuración de seguridad de Spring para la aplicación.
 * Esta clase define la cadena de filtros de seguridad y las reglas de autorización
 * para los endpoints de la API.
 */
@Configuration
public class SpringSecurityConfig {

    // Inyecta la configuracion de autenticacion de Spring Security
    private AuthenticationConfiguration authenticationConfiguration;
    private ObjectMapper objectMapper;

    public SpringSecurityConfig(AuthenticationConfiguration authenticationConfiguration, ObjectMapper objectMapper) {
        this.authenticationConfiguration = authenticationConfiguration;
        this.objectMapper = objectMapper;

    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Define la cadena de filtros de seguridad que se aplicará a las peticiones HTTP.
     */
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(authorize ->
                        authorize
                                // Permite el acceso público a la lista de usuarios paginada.
                                .requestMatchers(HttpMethod.GET, "/api/v1/users", "/api/v1/users/page/{page}").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/users/{id}").hasAnyRole("USER", "ADMIN")
                                .requestMatchers(HttpMethod.POST, "/api/v1/users").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/api/v1/users/{id}").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/api/v1/users/{id}").hasRole("ADMIN")
                                .anyRequest().authenticated())
                // Agrega el filtro de autenticación JWT
                .addFilter(new JwtAuthenticationFilter(authenticationManager(), objectMapper))
                // Deshabilita la protección CSRF, ya que se usan tokens JWT y no sesiones.
                .csrf(config -> config.disable())
                // Configura la gestión de sesiones como STATELESS, indicando que no se creará sesión en el servidor.
                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }
}

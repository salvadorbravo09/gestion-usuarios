package com.sbravoc.gestion_usuarios.infrastructure.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sbravoc.gestion_usuarios.domain.user.model.User;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.sbravoc.gestion_usuarios.infrastructure.security.TokenJwtConfig.SECRET_KEY;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;
    private ObjectMapper objectMapper;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, ObjectMapper objectMapper) {
        this.authenticationManager = authenticationManager;
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {

            // Leer las credenciales del cuerpo de la solicitud
            User credentials = objectMapper.readValue(request.getInputStream(), User.class);

            // Validar que las credenciales no sean nulas
            if (credentials.getUsername() == null || credentials.getPassword() == null) {
                throw new RuntimeException("Credenciales no pueden ser nulas");
            }

            // Crear un objeto UsernamePasswordAuthenticationToken con las credenciales
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    credentials.getUsername(),
                    credentials.getPassword()
            );

            // Autenticar al usuario
            return this.authenticationManager.authenticate(authenticationToken);
        } catch (IOException e) {
            throw new RuntimeException("Error al leer las credenciales", e);
        }
    }

    // Este metodo se llama cuando la autenticación es exitosa
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) authResult.getPrincipal();
        String username = user.getUsername();

        // Generar el token JWT
        String jwt = Jwts.builder()
                .subject(username)
                .signWith(SECRET_KEY)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 3600000)) // 1 hora de validez
                .compact();

        response.addHeader("Authorization", "Bearer " + jwt);

        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", jwt);
        tokenMap.put("username", username);
        tokenMap.put("message", "Autenticación exitosa");

        response.getWriter().write(objectMapper.writeValueAsString(tokenMap));
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        super.unsuccessfulAuthentication(request, response, failed);
    }
}


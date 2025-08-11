package com.sbravoc.gestion_usuarios.infrastructure.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sbravoc.gestion_usuarios.domain.user.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.sbravoc.gestion_usuarios.infrastructure.security.TokenJwtConfig.*;

/**
 * Filtro de autenticación de Spring Security para procesar los intentos de inicio de sesión
 * y generar un token JWT si las credenciales son válidas.
 * Este filtro se activa para una URL específica (generalmente /login) y es el primer paso
 * en el flujo de autenticación JWT.
 */
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final ObjectMapper objectMapper;

    /**
     * Constructor para inyectar las dependencias necesarias.
     */
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, ObjectMapper objectMapper) {
        this.authenticationManager = authenticationManager;
        this.objectMapper = objectMapper;
    }

    /**
     * Intenta autenticar al usuario a partir de las credenciales proporcionadas en la solicitud HTTP.
     * Lee el nombre de usuario y la contraseña del cuerpo de la solicitud, los empaqueta en un
     * {@link UsernamePasswordAuthenticationToken} y los delega al {@link AuthenticationManager}.
     *
     * @param request  La solicitud HTTP que contiene las credenciales.
     * @param response La respuesta HTTP.
     */
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

    /**
     * Se ejecuta cuando la autenticación del usuario es exitosa.
     * Este metodo genera el token JWT, incluyendo los claims (como nombre de usuario y roles),
     * y lo agrega a la cabecera de la respuesta HTTP. También escribe el token en el cuerpo
     * de la respuesta para facilitar su uso por parte del cliente.
     *
     * @param request    La solicitud original.
     * @param response   La respuesta HTTP.
     * @param chain      El objeto FilterChain para invocar el siguiente filtro.
     * @param authResult El objeto Authentication que representa al usuario autenticado.
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException {

        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) authResult.getPrincipal();
        String username = user.getUsername();
        Collection<? extends GrantedAuthority> roles = authResult.getAuthorities();

        Claims claims = Jwts.claims()
                .add("authorities", objectMapper.writeValueAsString(roles))
                .add("username", username)
                .build();

        // Generar el token JWT
        String jwt = Jwts.builder()
                .subject(username)
                .claims(claims)
                .signWith(SECRET_KEY)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 3600000)) // 1 hora de validez
                .compact();

        response.addHeader(HEADER_AUTHORIZATION, TOKEN_PREFIX + jwt);

        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", jwt);
        tokenMap.put("username", username);
        tokenMap.put("message", "Autenticación exitosa");

        response.getWriter().write(objectMapper.writeValueAsString(tokenMap));
        response.setContentType(CONTENT_TYPE);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    /**
     * Se ejecuta cuando la autenticación del usuario falla (por ejemplo, credenciales incorrectas).
     * Delega el manejo del error al comportamiento por defecto de la superclase, que generalmente
     * resulta en una respuesta 401 Unauthorized.
     *
     * @param request  La solicitud original.
     * @param response La respuesta HTTP.
     * @param failed   La excepción que causó el fallo de autenticación.
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        Map<String, String> body = new HashMap<>();
        body.put("message", "Credenciales inválidas");
        body.put("error", failed.getMessage());

        response.getWriter().write(objectMapper.writeValueAsString(body));
        response.setContentType(CONTENT_TYPE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}

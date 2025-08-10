package com.sbravoc.gestion_usuarios.infrastructure.security;

import com.sbravoc.gestion_usuarios.domain.user.model.User;
import com.sbravoc.gestion_usuarios.domain.user.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementación personalizada del servicio UserDetailsService de Spring Security.
 * Esta clase actúa como un adaptador entre Spring Security y el dominio de la aplicación,
 * permitiendo que Spring Security obtenga información de usuarios desde nuestro repositorio
 * de dominio para realizar la autenticación y autorización.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    /**
     * Repositorio de usuarios del dominio para acceder a los datos de usuario.
     */
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Carga un usuario por su nombre de usuario para la autenticación de Spring Security.
     * Este metodo es llamado por Spring Security durante el proceso de autenticación
     * para obtener los detalles del usuario, incluyendo sus credenciales y autoridades.
     *
     * @param username Nombre de usuario para buscar en el sistema
     * @return UserDetails Objeto con los detalles del usuario para Spring Security
     * @throws UsernameNotFoundException Si el usuario no se encuentra en el sistema
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Buscar el usuario en el repositorio de dominio
        Optional<User> existingUser = this.userRepository.findByUsername(username);

        // Validar que el usuario existe
        if (existingUser.isEmpty()) {
            throw new UsernameNotFoundException("Usuario con nombre de usuario " + username + " no encontrado.");
        }

        // Obtener el usuario del Optional
        User user = existingUser.get();

        // Convertir los roles del dominio a autoridades de Spring Security
        List<GrantedAuthority> authorities = user.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());

        // Crear y retornar el objeto UserDetails de Spring Security
        return new org.springframework.security.core.userdetails.User(
                username,                    // Nombre de usuario
                user.getPassword(),          // Contraseña encriptada
                true,                        // Cuenta habilitada
                true,                        // Cuenta no expirada
                true,                        // Credenciales no expiradas
                true,                        // Cuenta no bloqueada
                authorities                  // Lista de autoridades/roles
        );
    }
}

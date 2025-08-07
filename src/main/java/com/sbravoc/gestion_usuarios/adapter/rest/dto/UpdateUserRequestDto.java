package com.sbravoc.gestion_usuarios.adapter.rest.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequestDto {

    @Size(max = 255, message = "El nombre no puede exceder 255 caracteres")
    private String name;

    @Size(max = 255, message = "El apellido no puede exceder 255 caracteres")
    private String lastName;

    @Email(message = "El correo electrónico debe ser válido")
    @Size(max = 255, message = "El email no puede exceder 255 caracteres")
    private String email;

    @Size(min = 3, max = 255, message = "El username debe tener entre 3 y 255 caracteres")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "El username solo puede contener letras, números y guiones bajos")
    private String username;

    @Size(min = 4, max = 255, message = "La contraseña debe tener entre 4 y 255 caracteres")
    private String password;
}

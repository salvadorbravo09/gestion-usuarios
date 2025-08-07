package com.sbravoc.gestion_usuarios.adapter.rest.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
public class CreateUserRequestDto {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 255, message = "El nombre no puede exceder los 255 caracteres")
    private String name;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 255, message = "El apellido no puede exceder los 255 caracteres")
    private String lastName;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El correo electronico debe ser valido")
    @Size(max = 255, message = "El correo electronico no puede exceder los 255 caracteres")
    private String email;

    @NotBlank(message = "El username es obligatorio")
    @Size(min = 3, max = 255, message = "El username debe tener entre 3 y 255 caracteres")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "El username solo puede contener letras, números y guiones bajos")
    private String username;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 4, max = 255, message = "La contraseña debe tener entre 4 y 255 caracteres")
    private String password;
}

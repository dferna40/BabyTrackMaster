package dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class RegistroUsuarioDTO {

    @NotBlank @Size(max = 80)
    private String nombre;

    @Size(max = 120)
    private String apellidos;

    @Email @NotBlank @Size(max = 160)
    private String email;

    @NotBlank @Size(min = 8, max = 64)
    private String password;
}

package br.com.ecoconnect.dto;

import br.com.ecoconnect.model.TipoUsuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** RN02: cadastro contém nome, e-mail e senha. */
public record CadastroUsuarioRequest(
        @NotBlank(message = "Nome é obrigatório") String nome,
        @NotBlank(message = "E-mail é obrigatório") @Email(message = "E-mail inválido") String email,
        @NotBlank(message = "Senha é obrigatória") @Size(min = 6, message = "A senha deve ter ao menos 6 caracteres") String senha,
        TipoUsuario tipo) {
}

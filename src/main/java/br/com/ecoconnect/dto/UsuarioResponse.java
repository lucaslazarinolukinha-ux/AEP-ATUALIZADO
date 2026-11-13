package br.com.ecoconnect.dto;

import br.com.ecoconnect.model.TipoUsuario;
import br.com.ecoconnect.model.Usuario;

/** Nunca expõe a senha (RN12). */
public record UsuarioResponse(Long id, String nome, String email, TipoUsuario tipo) {

    public static UsuarioResponse de(Usuario u) {
        return new UsuarioResponse(u.getId(), u.getNome(), u.getEmail(), u.getTipo());
    }
}

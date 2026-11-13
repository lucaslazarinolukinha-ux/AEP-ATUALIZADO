package br.com.ecoconnect.dto;

import jakarta.validation.constraints.NotBlank;

public record RecicladorRequest(
        @NotBlank(message = "Nome é obrigatório") String nome,
        String razaoSocial,
        String cnpj,
        @NotBlank(message = "Telefone é obrigatório") String telefone,
        String email,
        String horarioAtendimento) {
}

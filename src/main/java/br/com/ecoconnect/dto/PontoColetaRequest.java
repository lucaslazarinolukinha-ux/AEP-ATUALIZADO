package br.com.ecoconnect.dto;

import br.com.ecoconnect.model.Endereco;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record PontoColetaRequest(
        @NotBlank(message = "Nome é obrigatório") String nome,
        String descricao,
        @NotBlank(message = "Horário de funcionamento é obrigatório") String horarioFuncionamento,
        @NotNull(message = "Endereço é obrigatório") @Valid Endereco endereco,
        @NotEmpty(message = "Informe ao menos um material aceito") Set<Long> materiaisIds) {
}

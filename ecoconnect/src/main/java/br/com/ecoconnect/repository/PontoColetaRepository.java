package br.com.ecoconnect.repository;

import br.com.ecoconnect.model.PontoColeta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PontoColetaRepository extends JpaRepository<PontoColeta, Long> {

    // RF03: pesquisa por localização e/ou tipo de material (filtros opcionais)
    @Query("""
            SELECT DISTINCT p FROM PontoColeta p
            LEFT JOIN p.materiaisAceitos m
            WHERE p.status = true
              AND (:cidade IS NULL OR LOWER(p.endereco.cidade) LIKE LOWER(CONCAT('%', :cidade, '%')))
              AND (:bairro IS NULL OR LOWER(p.endereco.bairro) LIKE LOWER(CONCAT('%', :bairro, '%')))
              AND (:material IS NULL OR LOWER(m.nome) LIKE LOWER(CONCAT('%', :material, '%')))
            """)
    List<PontoColeta> pesquisar(@Param("cidade") String cidade,
                                @Param("bairro") String bairro,
                                @Param("material") String material);

    // RN09: evita ponto duplicado (mesmo nome, mesmo CEP e número)
    boolean existsByNomeIgnoreCaseAndEnderecoCepAndEnderecoNumero(String nome, String cep, String numero);
}

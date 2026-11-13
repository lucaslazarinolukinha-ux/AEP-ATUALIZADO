package br.com.ecoconnect.repository;

import br.com.ecoconnect.model.Reciclador;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecicladorRepository extends JpaRepository<Reciclador, Long> {
    boolean existsByCnpj(String cnpj);
    boolean existsByUsuarioId(Long idUsuario);
}

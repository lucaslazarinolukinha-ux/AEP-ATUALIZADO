package br.com.ecoconnect.repository;

import br.com.ecoconnect.model.Material;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaterialRepository extends JpaRepository<Material, Long> {
}

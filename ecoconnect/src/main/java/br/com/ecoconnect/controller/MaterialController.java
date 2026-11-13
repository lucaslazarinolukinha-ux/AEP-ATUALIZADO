package br.com.ecoconnect.controller;

import br.com.ecoconnect.model.Material;
import br.com.ecoconnect.repository.MaterialRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/materiais")
public class MaterialController {

    private final MaterialRepository repository;

    public MaterialController(MaterialRepository repository) {
        this.repository = repository;
    }

    /** Lista os materiais pré-cadastrados (RN06) para uso nos formulários. */
    @GetMapping
    public List<Material> listar() {
        return repository.findAll();
    }
}

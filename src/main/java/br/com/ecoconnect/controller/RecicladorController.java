package br.com.ecoconnect.controller;

import br.com.ecoconnect.dto.RecicladorRequest;
import br.com.ecoconnect.model.Reciclador;
import br.com.ecoconnect.service.RecicladorService;
import br.com.ecoconnect.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recicladores")
public class RecicladorController {

    private final RecicladorService service;
    private final UsuarioService usuarioService;

    public RecicladorController(RecicladorService service, UsuarioService usuarioService) {
        this.service = service;
        this.usuarioService = usuarioService;
    }

    /** RF04 / RN07: consulta exige usuário autenticado */
    @GetMapping
    public List<Reciclador> listar(@RequestHeader(value = "X-Usuario-Id", required = false) Long idAutenticado) {
        usuarioService.exigirUsuario(idAutenticado);
        return service.listar();
    }

    @GetMapping("/{id}")
    public Reciclador buscar(@PathVariable Long id,
                             @RequestHeader(value = "X-Usuario-Id", required = false) Long idAutenticado) {
        usuarioService.exigirUsuario(idAutenticado);
        return service.buscar(id);
    }

    @PostMapping
    public ResponseEntity<Reciclador> cadastrar(@Valid @RequestBody RecicladorRequest req,
                                                @RequestHeader(value = "X-Usuario-Id", required = false) Long idAutenticado) {
        var gestor = usuarioService.exigirGestor(idAutenticado);
        return ResponseEntity.status(HttpStatus.CREATED).body(service.cadastrar(req, gestor));
    }

    @PutMapping("/{id}")
    public Reciclador atualizar(@PathVariable Long id,
                                @Valid @RequestBody RecicladorRequest req,
                                @RequestHeader(value = "X-Usuario-Id", required = false) Long idAutenticado) {
        var gestor = usuarioService.exigirGestor(idAutenticado);
        return service.atualizar(id, req, gestor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id,
                                        @RequestHeader(value = "X-Usuario-Id", required = false) Long idAutenticado) {
        var gestor = usuarioService.exigirGestor(idAutenticado);
        service.excluir(id, gestor);
        return ResponseEntity.noContent().build();
    }
}

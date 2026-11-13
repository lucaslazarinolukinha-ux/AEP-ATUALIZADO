package br.com.ecoconnect.controller;

import br.com.ecoconnect.dto.PontoColetaRequest;
import br.com.ecoconnect.model.PontoColeta;
import br.com.ecoconnect.service.PontoColetaService;
import br.com.ecoconnect.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pontos")
public class PontoColetaController {

    private final PontoColetaService service;
    private final UsuarioService usuarioService;

    public PontoColetaController(PontoColetaService service, UsuarioService usuarioService) {
        this.service = service;
        this.usuarioService = usuarioService;
    }

    /** RF02: consultar pontos cadastrados */
    @GetMapping
    public List<PontoColeta> listar() {
        return service.listar();
    }

    /** RF03: pesquisar por localização ou material. Ex: /api/pontos/pesquisa?cidade=Maringá&material=Vidro */
    @GetMapping("/pesquisa")
    public List<PontoColeta> pesquisar(@RequestParam(required = false) String cidade,
                                       @RequestParam(required = false) String bairro,
                                       @RequestParam(required = false) String material) {
        return service.pesquisar(cidade, bairro, material);
    }

    /** RF05: detalhes (materiais aceitos, localização, horários) */
    @GetMapping("/{id}")
    public PontoColeta buscar(@PathVariable Long id) {
        return service.buscar(id);
    }

    @PostMapping
    public ResponseEntity<PontoColeta> cadastrar(@Valid @RequestBody PontoColetaRequest req,
                                                 @RequestHeader(value = "X-Usuario-Id", required = false) Long idAutenticado) {
        var gestor = usuarioService.exigirGestor(idAutenticado);
        return ResponseEntity.status(HttpStatus.CREATED).body(service.cadastrar(req, gestor));
    }

    @PutMapping("/{id}")
    public PontoColeta atualizar(@PathVariable Long id,
                                 @Valid @RequestBody PontoColetaRequest req,
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

package br.com.ecoconnect.controller;

import br.com.ecoconnect.dto.CadastroUsuarioRequest;
import br.com.ecoconnect.dto.LoginRequest;
import br.com.ecoconnect.dto.UsuarioResponse;
import br.com.ecoconnect.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final UsuarioService service;

    public AuthController(UsuarioService service) {
        this.service = service;
    }

    /** RF01: cadastro */
    @PostMapping("/usuarios")
    public ResponseEntity<UsuarioResponse> cadastrar(@Valid @RequestBody CadastroUsuarioRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioResponse.de(service.cadastrar(req)));
    }

    /** RF01: login. Use o "id" retornado no header X-Usuario-Id nas demais chamadas. */
    @PostMapping("/login")
    public UsuarioResponse login(@Valid @RequestBody LoginRequest req) {
        return UsuarioResponse.de(service.autenticar(req));
    }

    /** RN11: administrador lista usuários */
    @GetMapping("/usuarios")
    public List<UsuarioResponse> listar(@RequestHeader(value = "X-Usuario-Id", required = false) Long idAutenticado) {
        service.exigirAdmin(idAutenticado);
        return service.listar().stream().map(UsuarioResponse::de).toList();
    }

    /** RN11: administrador exclui usuário */
    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id,
                                        @RequestHeader(value = "X-Usuario-Id", required = false) Long idAutenticado) {
        service.exigirAdmin(idAutenticado);
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}

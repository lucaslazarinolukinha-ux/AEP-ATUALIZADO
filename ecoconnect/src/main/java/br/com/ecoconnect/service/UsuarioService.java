package br.com.ecoconnect.service;

import br.com.ecoconnect.dto.CadastroUsuarioRequest;
import br.com.ecoconnect.dto.LoginRequest;
import br.com.ecoconnect.exception.RegraNegocioException;
import br.com.ecoconnect.model.TipoUsuario;
import br.com.ecoconnect.model.Usuario;
import br.com.ecoconnect.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;
    private final PasswordEncoder encoder;

    public UsuarioService(UsuarioRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    /** RF01 / RN01-RN03 / RN12 */
    public Usuario cadastrar(CadastroUsuarioRequest req) {
        String email = req.email().trim().toLowerCase();
        if (repository.existsByEmail(email)) {
            throw new RegraNegocioException("Já existe um usuário cadastrado com este e-mail");
        }
        Usuario u = new Usuario();
        u.setNome(req.nome().trim());
        u.setEmail(email);
        u.setSenha(encoder.encode(req.senha()));
        // Auto-cadastro público nunca cria administrador
        TipoUsuario tipo = req.tipo() == null || req.tipo() == TipoUsuario.ADMINISTRADOR
                ? TipoUsuario.CIDADAO : req.tipo();
        u.setTipo(tipo);
        return repository.save(u);
    }

    /** RF01: autenticação */
    public Usuario autenticar(LoginRequest req) {
        Usuario u = repository.findByEmail(req.email().trim().toLowerCase())
                .orElseThrow(() -> new RegraNegocioException("E-mail ou senha inválidos", HttpStatus.UNAUTHORIZED));
        if (!encoder.matches(req.senha(), u.getSenha())) {
            throw new RegraNegocioException("E-mail ou senha inválidos", HttpStatus.UNAUTHORIZED);
        }
        return u;
    }

    /** RN01: exige usuário válido (identificado pelo header X-Usuario-Id). */
    public Usuario exigirUsuario(Long id) {
        if (id == null) {
            throw new RegraNegocioException("Autenticação necessária", HttpStatus.UNAUTHORIZED);
        }
        return repository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("Usuário inválido", HttpStatus.UNAUTHORIZED));
    }

    /** RN08: só RECICLADOR ou ADMINISTRADOR gerenciam pontos/recicladores. */
    public Usuario exigirGestor(Long id) {
        Usuario u = exigirUsuario(id);
        if (u.getTipo() == TipoUsuario.CIDADAO) {
            throw new RegraNegocioException("Usuário sem permissão para esta operação", HttpStatus.FORBIDDEN);
        }
        return u;
    }

    /** RN11 */
    public Usuario exigirAdmin(Long id) {
        Usuario u = exigirUsuario(id);
        if (u.getTipo() != TipoUsuario.ADMINISTRADOR) {
            throw new RegraNegocioException("Apenas administradores podem executar esta operação", HttpStatus.FORBIDDEN);
        }
        return u;
    }

    public List<Usuario> listar() {
        return repository.findAll();
    }

    public void excluir(Long id) {
        if (!repository.existsById(id)) {
            throw new RegraNegocioException("Usuário não encontrado", HttpStatus.NOT_FOUND);
        }
        repository.deleteById(id);
    }
}

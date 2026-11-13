package br.com.ecoconnect.service;

import br.com.ecoconnect.dto.RecicladorRequest;
import br.com.ecoconnect.exception.RegraNegocioException;
import br.com.ecoconnect.model.Reciclador;
import br.com.ecoconnect.model.TipoUsuario;
import br.com.ecoconnect.model.Usuario;
import br.com.ecoconnect.repository.RecicladorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecicladorService {

    private final RecicladorRepository repository;

    public RecicladorService(RecicladorRepository repository) {
        this.repository = repository;
    }

    /** RF04: consulta (RN07: dados de contato visíveis a usuários autenticados). */
    public List<Reciclador> listar() {
        return repository.findAll();
    }

    public Reciclador buscar(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RegraNegocioException("Reciclador não encontrado", HttpStatus.NOT_FOUND));
    }

    /** RF04: cadastro */
    public Reciclador cadastrar(RecicladorRequest req, Usuario gestor) {
        // RN09
        if (repository.existsByUsuarioId(gestor.getId())) {
            throw new RegraNegocioException("Este usuário já possui um cadastro de reciclador");
        }
        if (req.cnpj() != null && !req.cnpj().isBlank() && repository.existsByCnpj(req.cnpj())) {
            throw new RegraNegocioException("Já existe um reciclador com este CNPJ");
        }
        Reciclador r = new Reciclador();
        r.setUsuario(gestor);
        preencher(r, req);
        return repository.save(r);
    }

    public Reciclador atualizar(Long id, RecicladorRequest req, Usuario gestor) {
        Reciclador r = buscar(id);
        boolean dono = r.getUsuario().getId().equals(gestor.getId());
        if (!dono && gestor.getTipo() != TipoUsuario.ADMINISTRADOR) {
            throw new RegraNegocioException("Apenas o próprio reciclador pode alterar seus dados", HttpStatus.FORBIDDEN);
        }
        preencher(r, req);
        return repository.save(r);
    }

    public void excluir(Long id, Usuario gestor) {
        Reciclador r = buscar(id);
        boolean dono = r.getUsuario().getId().equals(gestor.getId());
        if (!dono && gestor.getTipo() != TipoUsuario.ADMINISTRADOR) {
            throw new RegraNegocioException("Sem permissão para excluir este reciclador", HttpStatus.FORBIDDEN);
        }
        repository.delete(r);
    }

    private void preencher(Reciclador r, RecicladorRequest req) {
        r.setNome(req.nome().trim());
        r.setRazaoSocial(req.razaoSocial());
        r.setCnpj(req.cnpj() == null || req.cnpj().isBlank() ? null : req.cnpj());
        r.setTelefone(req.telefone());
        r.setEmail(req.email());
        r.setHorarioAtendimento(req.horarioAtendimento());
    }
}

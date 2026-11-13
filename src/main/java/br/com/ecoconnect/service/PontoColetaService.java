package br.com.ecoconnect.service;

import br.com.ecoconnect.dto.PontoColetaRequest;
import br.com.ecoconnect.exception.RegraNegocioException;
import br.com.ecoconnect.model.Material;
import br.com.ecoconnect.model.PontoColeta;
import br.com.ecoconnect.model.Usuario;
import br.com.ecoconnect.model.TipoUsuario;
import br.com.ecoconnect.repository.MaterialRepository;
import br.com.ecoconnect.repository.PontoColetaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class PontoColetaService {

    private final PontoColetaRepository pontos;
    private final MaterialRepository materiais;

    public PontoColetaService(PontoColetaRepository pontos, MaterialRepository materiais) {
        this.pontos = pontos;
        this.materiais = materiais;
    }

    /** RF02 */
    @Transactional(readOnly = true)
    public List<PontoColeta> listar() {
        return pontos.findAll();
    }

    /** RF05 */
    @Transactional(readOnly = true)
    public PontoColeta buscar(Long id) {
        return pontos.findById(id)
                .orElseThrow(() -> new RegraNegocioException("Ponto de coleta não encontrado", HttpStatus.NOT_FOUND));
    }

    /** RF03 */
    @Transactional(readOnly = true)
    public List<PontoColeta> pesquisar(String cidade, String bairro, String material) {
        return pontos.pesquisar(vazioParaNulo(cidade), vazioParaNulo(bairro), vazioParaNulo(material));
    }

    @Transactional
    public PontoColeta cadastrar(PontoColetaRequest req, Usuario gestor) {
        // RN09
        if (pontos.existsByNomeIgnoreCaseAndEnderecoCepAndEnderecoNumero(
                req.nome(), req.endereco().getCep(), req.endereco().getNumero())) {
            throw new RegraNegocioException("Já existe um ponto de coleta com este nome e endereço");
        }
        PontoColeta p = new PontoColeta();
        p.setCadastradoPor(gestor);
        preencher(p, req);
        return pontos.save(p);
    }

    @Transactional
    public PontoColeta atualizar(Long id, PontoColetaRequest req, Usuario gestor) {
        PontoColeta p = buscar(id);
        // RN08/RN10: só o responsável pelo cadastro ou um administrador altera
        boolean dono = p.getCadastradoPor().getId().equals(gestor.getId());
        if (!dono && gestor.getTipo() != TipoUsuario.ADMINISTRADOR) {
            throw new RegraNegocioException("Apenas o responsável pelo ponto pode alterá-lo", HttpStatus.FORBIDDEN);
        }
        preencher(p, req);
        return pontos.save(p);
    }

    @Transactional
    public void excluir(Long id, Usuario gestor) {
        PontoColeta p = buscar(id);
        boolean dono = p.getCadastradoPor().getId().equals(gestor.getId());
        if (!dono && gestor.getTipo() != TipoUsuario.ADMINISTRADOR) {
            throw new RegraNegocioException("Apenas o responsável pelo ponto pode excluí-lo", HttpStatus.FORBIDDEN);
        }
        pontos.delete(p);
    }

    private void preencher(PontoColeta p, PontoColetaRequest req) {
        p.setNome(req.nome().trim());
        p.setDescricao(req.descricao());
        p.setHorarioFuncionamento(req.horarioFuncionamento());
        if (p.getEndereco() != null) {
            req.endereco().setId(p.getEndereco().getId());
        }
        p.setEndereco(req.endereco());

        // RN06: só aceita materiais já cadastrados
        Set<Material> aceitos = new HashSet<>(materiais.findAllById(req.materiaisIds()));
        if (aceitos.size() != req.materiaisIds().size()) {
            throw new RegraNegocioException("Um ou mais materiais informados não estão cadastrados");
        }
        p.setMateriaisAceitos(aceitos);
    }

    private String vazioParaNulo(String s) {
        return (s == null || s.isBlank()) ? null : s.trim();
    }
}

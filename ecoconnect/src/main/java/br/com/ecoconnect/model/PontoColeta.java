package br.com.ecoconnect.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ponto_coleta")
public class PontoColeta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ponto")
    private Long id;

    @Column(nullable = false, length = 150)
    private String nome;

    private String descricao;

    // RN04: todo ponto tem horário de funcionamento
    @Column(name = "horario_funcionamento", nullable = false)
    private String horarioFuncionamento;

    private boolean status = true;

    // RN04: todo ponto tem localização
    @OneToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "id_endereco", nullable = false)
    private Endereco endereco;

    // RN05: um ponto aceita vários materiais / RN06: materiais já cadastrados
    // EAGER: com open-in-view=false, a lista precisa vir carregada para o JSON (evita LazyInitializationException)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "ponto_material",
            joinColumns = @JoinColumn(name = "id_ponto"),
            inverseJoinColumns = @JoinColumn(name = "id_material"))
    private Set<Material> materiaisAceitos = new HashSet<>();

    // Usuário que cadastrou o ponto
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_usuario", nullable = false)
    @JsonIgnore
    private Usuario cadastradoPor;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public String getHorarioFuncionamento() { return horarioFuncionamento; }
    public void setHorarioFuncionamento(String h) { this.horarioFuncionamento = h; }
    public boolean isStatus() { return status; }
    public void setStatus(boolean status) { this.status = status; }
    public Endereco getEndereco() { return endereco; }
    public void setEndereco(Endereco endereco) { this.endereco = endereco; }
    public Set<Material> getMateriaisAceitos() { return materiaisAceitos; }
    public void setMateriaisAceitos(Set<Material> m) { this.materiaisAceitos = m; }
    public Usuario getCadastradoPor() { return cadastradoPor; }
    public void setCadastradoPor(Usuario u) { this.cadastradoPor = u; }
}

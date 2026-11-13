package br.com.ecoconnect.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "reciclador")
public class Reciclador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reciclador")
    private Long id;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(name = "razao_social")
    private String razaoSocial;

    // RN09: impede duplicidade
    @Column(unique = true, length = 18)
    private String cnpj;

    // Dados de contato (RF04)
    private String telefone;
    private String email;

    @Column(name = "horario_atendimento")
    private String horarioAtendimento;

    // Usuário dono do cadastro
    @OneToOne(optional = false)
    @JoinColumn(name = "id_usuario", nullable = false, unique = true)
    @JsonIgnore
    private Usuario usuario;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getRazaoSocial() { return razaoSocial; }
    public void setRazaoSocial(String r) { this.razaoSocial = r; }
    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getHorarioAtendimento() { return horarioAtendimento; }
    public void setHorarioAtendimento(String h) { this.horarioAtendimento = h; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
}

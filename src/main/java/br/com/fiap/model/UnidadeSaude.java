package br.com.fiap.model;

public class UnidadeSaude {
    private Long id;
    private String cnes;
    private String nome;
    private String bairro;
    private String endereco;
    private String referencia;
    private String descricao;
    private Boolean ativa;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCnes() { return cnes; }
    public void setCnes(String cnes) { this.cnes = cnes; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getBairro() { return bairro; }
    public void setBairro(String bairro) { this.bairro = bairro; }
    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    public String getReferencia() { return referencia; }
    public void setReferencia(String referencia) { this.referencia = referencia; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public Boolean getAtiva() { return ativa; }
    public void setAtiva(Boolean ativa) { this.ativa = ativa; }
}

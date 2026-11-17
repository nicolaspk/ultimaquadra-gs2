package br.com.fiap.dto;

public class TrechoSaida {
    private Long id;
    private Long unidadeId;
    private String nome;
    private String descricao;
    private String distanciaMediaEntrada;
    private Boolean ativo;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUnidadeId() { return unidadeId; }
    public void setUnidadeId(Long unidadeId) { this.unidadeId = unidadeId; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public String getDistanciaMediaEntrada() { return distanciaMediaEntrada; }
    public void setDistanciaMediaEntrada(String distanciaMediaEntrada) { this.distanciaMediaEntrada = distanciaMediaEntrada; }
    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }
}

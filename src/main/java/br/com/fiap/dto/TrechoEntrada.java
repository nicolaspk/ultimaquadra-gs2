package br.com.fiap.dto;

import jakarta.validation.constraints.*;

public class TrechoEntrada {
    @NotNull
    @Positive
    private Long unidadeId;
    @NotBlank
    @Size(max=160)
    private String nome;
    @NotBlank
    @Size(max=600)
    private String descricao;
    @NotBlank
    @Size(max=100)
    private String distanciaMediaEntrada;
    @NotNull
    private Boolean ativo;

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

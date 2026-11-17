package br.com.fiap.dto;

import java.time.LocalDate;

public class OcorrenciaSaida {
    private Long id;
    private Long trechoId;
    private Long unidadeId;
    private String categoria;
    private String descricao;
    private String referenciaLocal;
    private String passagem;
    private String situacao;
    private LocalDate dataRelato;
    private String larguraLivre;
    private String alturaDesnivel;
    private String distanciaEntrada;
    private String impedimento;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getTrechoId() { return trechoId; }
    public void setTrechoId(Long trechoId) { this.trechoId = trechoId; }
    public Long getUnidadeId() { return unidadeId; }
    public void setUnidadeId(Long unidadeId) { this.unidadeId = unidadeId; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public String getReferenciaLocal() { return referenciaLocal; }
    public void setReferenciaLocal(String referenciaLocal) { this.referenciaLocal = referenciaLocal; }
    public String getPassagem() { return passagem; }
    public void setPassagem(String passagem) { this.passagem = passagem; }
    public String getSituacao() { return situacao; }
    public void setSituacao(String situacao) { this.situacao = situacao; }
    public LocalDate getDataRelato() { return dataRelato; }
    public void setDataRelato(LocalDate dataRelato) { this.dataRelato = dataRelato; }
    public String getLarguraLivre() { return larguraLivre; }
    public void setLarguraLivre(String larguraLivre) { this.larguraLivre = larguraLivre; }
    public String getAlturaDesnivel() { return alturaDesnivel; }
    public void setAlturaDesnivel(String alturaDesnivel) { this.alturaDesnivel = alturaDesnivel; }
    public String getDistanciaEntrada() { return distanciaEntrada; }
    public void setDistanciaEntrada(String distanciaEntrada) { this.distanciaEntrada = distanciaEntrada; }
    public String getImpedimento() { return impedimento; }
    public void setImpedimento(String impedimento) { this.impedimento = impedimento; }
}

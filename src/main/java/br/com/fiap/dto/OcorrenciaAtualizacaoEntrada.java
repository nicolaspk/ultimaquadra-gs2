package br.com.fiap.dto;

import jakarta.validation.constraints.*;

public class OcorrenciaAtualizacaoEntrada {
    @NotNull
    @Positive
    private Long trechoId;
    @NotNull
    @Positive
    private Long unidadeId;
    @NotBlank
    @Pattern(regexp="Buraco|Desnível|Passagem estreita|Obstáculo fixo|Obstáculo temporário|Pavimentação irregular|Ausência de rebaixamento|Outros", message="Categoria invalida")
    private String categoria;
    @NotBlank
    @Size(min=15, max=1500)
    private String descricao;
    @Size(max=350)
    private String referenciaLocal;
    @Pattern(regexp="Dificultada|Bloqueada|Livre|Não sei informar", message="Condicao de passagem invalida")
    private String passagem;
    @Size(max=100)
    private String larguraLivre;
    @Size(max=100)
    private String alturaDesnivel;
    @Size(max=100)
    private String distanciaEntrada;
    @Pattern(regexp="Parcial|Total")
    private String impedimento;
    @NotBlank
    @Pattern(regexp="Relatada|Em verificação|Confirmada|Não confirmada|Resolvida", message="Situacao invalida")
    private String situacao;

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
    public String getLarguraLivre() { return larguraLivre; }
    public void setLarguraLivre(String larguraLivre) { this.larguraLivre = larguraLivre; }
    public String getAlturaDesnivel() { return alturaDesnivel; }
    public void setAlturaDesnivel(String alturaDesnivel) { this.alturaDesnivel = alturaDesnivel; }
    public String getDistanciaEntrada() { return distanciaEntrada; }
    public void setDistanciaEntrada(String distanciaEntrada) { this.distanciaEntrada = distanciaEntrada; }
    public String getImpedimento() { return impedimento; }
    public void setImpedimento(String impedimento) { this.impedimento = impedimento; }
    public String getSituacao() { return situacao; }
    public void setSituacao(String situacao) { this.situacao = situacao; }
}

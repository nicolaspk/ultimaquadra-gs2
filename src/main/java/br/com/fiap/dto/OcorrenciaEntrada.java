package br.com.fiap.dto;

import jakarta.validation.constraints.*;

public class OcorrenciaEntrada {
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
}

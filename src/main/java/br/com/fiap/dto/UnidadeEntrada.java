package br.com.fiap.dto;

import jakarta.validation.constraints.*;

public class UnidadeEntrada {
    @NotBlank
    @Pattern(regexp="\\d{7}", message="CNES deve ter 7 digitos")
    private String cnes;
    @NotBlank
    @Size(max=160)
    private String nome;
    @NotBlank
    @Size(max=80)
    private String bairro;
    @NotBlank
    @Size(max=260)
    private String endereco;
    @NotBlank
    @Size(max=180)
    private String referencia;
    @NotBlank
    @Size(max=500)
    private String descricao;
    @NotNull
    private Boolean ativa;

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

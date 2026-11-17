package br.com.fiap.dto;
import java.time.Instant;
import java.util.List;
public record ApiErro(Instant instante, int status, String erro, String mensagem, String caminho, List<String> detalhes) {}

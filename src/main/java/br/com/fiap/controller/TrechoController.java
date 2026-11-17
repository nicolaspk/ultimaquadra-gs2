package br.com.fiap.controller;

import br.com.fiap.bo.OcorrenciaBO;
import br.com.fiap.bo.TrechoAcessoBO;
import br.com.fiap.dto.OcorrenciaEntrada;
import br.com.fiap.dto.OcorrenciaSaida;
import br.com.fiap.dto.TrechoEntrada;
import br.com.fiap.dto.TrechoSaida;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@RestController
public class TrechoController {
    private final TrechoAcessoBO trechos;
    private final OcorrenciaBO ocorrencias;

    public TrechoController(TrechoAcessoBO trechos, OcorrenciaBO ocorrencias) {
        this.trechos = trechos;
        this.ocorrencias = ocorrencias;
    }

    @GetMapping("/api/trechos")
    public List<TrechoSaida> listar() { return trechos.listar(); }

    @GetMapping("/api/trechos/{id}")
    public TrechoSaida buscar(@PathVariable Long id) { return trechos.buscar(id); }

    @GetMapping("/api/trechos/{id}/ocorrencias")
    public List<OcorrenciaSaida> listarOcorrencias(@PathVariable Long id) {
        return ocorrencias.listarPorTrecho(id);
    }

    @PostMapping("/api/trechos/{id}/ocorrencias")
    public ResponseEntity<OcorrenciaSaida> cadastrarOcorrencia(@PathVariable Long id, @Valid @RequestBody OcorrenciaEntrada entrada) {
        OcorrenciaSaida nova = ocorrencias.cadastrarNoTrecho(id, entrada);
        return ResponseEntity.created(URI.create("/api/ocorrencias/" + nova.getId())).body(nova);
    }

    @PostMapping("/api/trechos")
    public ResponseEntity<TrechoSaida> cadastrar(@Valid @RequestBody TrechoEntrada entrada) {
        TrechoSaida novo = trechos.cadastrar(entrada);
        return ResponseEntity.created(URI.create("/api/trechos/" + novo.getId())).body(novo);
    }

    @PutMapping("/api/trechos/{id}")
    public TrechoSaida atualizar(@PathVariable Long id, @Valid @RequestBody TrechoEntrada entrada) {
        return trechos.atualizar(id, entrada);
    }

    @DeleteMapping("/api/trechos/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        trechos.remover(id);
        return ResponseEntity.noContent().build();
    }
}

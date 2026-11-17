package br.com.fiap.controller;

import br.com.fiap.bo.TrechoAcessoBO;
import br.com.fiap.bo.UnidadeSaudeBO;
import br.com.fiap.dto.TrechoEntrada;
import br.com.fiap.dto.TrechoSaida;
import br.com.fiap.dto.UnidadeEntrada;
import br.com.fiap.dto.UnidadeSaida;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@RestController
public class UnidadeController {
    private final UnidadeSaudeBO unidades;
    private final TrechoAcessoBO trechos;

    public UnidadeController(UnidadeSaudeBO unidades, TrechoAcessoBO trechos) {
        this.unidades = unidades;
        this.trechos = trechos;
    }

    @GetMapping("/api/unidades")
    public List<UnidadeSaida> listar() { return unidades.listar(); }

    @GetMapping("/api/unidades/{id}")
    public UnidadeSaida buscar(@PathVariable Long id) { return unidades.buscar(id); }

    @GetMapping("/api/unidades/{id}/trechos")
    public List<TrechoSaida> listarTrechos(@PathVariable Long id) { return trechos.listarPorUnidade(id); }

    @PostMapping("/api/unidades")
    public ResponseEntity<UnidadeSaida> cadastrar(@Valid @RequestBody UnidadeEntrada entrada) {
        UnidadeSaida nova = unidades.cadastrar(entrada);
        return ResponseEntity.created(URI.create("/api/unidades/" + nova.getId())).body(nova);
    }

    @PutMapping("/api/unidades/{id}")
    public UnidadeSaida atualizar(@PathVariable Long id, @Valid @RequestBody UnidadeEntrada entrada) {
        return unidades.atualizar(id, entrada);
    }

    @DeleteMapping("/api/unidades/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        unidades.remover(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/api/unidades/{id}/trechos")
    public ResponseEntity<TrechoSaida> cadastrarTrecho(@PathVariable Long id, @Valid @RequestBody TrechoEntrada entrada) {
        TrechoSaida novo = trechos.cadastrarNaUnidade(id, entrada);
        return ResponseEntity.created(URI.create("/api/trechos/" + novo.getId())).body(novo);
    }
}

package br.com.fiap.controller;

import br.com.fiap.bo.OcorrenciaBO;
import br.com.fiap.dto.OcorrenciaAtualizacaoEntrada;
import br.com.fiap.dto.OcorrenciaEntrada;
import br.com.fiap.dto.OcorrenciaSaida;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@RestController
public class OcorrenciaController {
    private final OcorrenciaBO ocorrencias;

    public OcorrenciaController(OcorrenciaBO ocorrencias) { this.ocorrencias = ocorrencias; }

    @GetMapping("/api/ocorrencias")
    public List<OcorrenciaSaida> listar() { return ocorrencias.listar(); }

    @GetMapping("/api/ocorrencias/{id}")
    public OcorrenciaSaida buscar(@PathVariable Long id) { return ocorrencias.buscar(id); }

    @PostMapping("/api/ocorrencias")
    public ResponseEntity<OcorrenciaSaida> cadastrar(@Valid @RequestBody OcorrenciaEntrada entrada) {
        OcorrenciaSaida nova = ocorrencias.cadastrar(entrada);
        return ResponseEntity.created(URI.create("/api/ocorrencias/" + nova.getId())).body(nova);
    }

    @PutMapping("/api/ocorrencias/{id}")
    public OcorrenciaSaida atualizar(@PathVariable Long id, @Valid @RequestBody OcorrenciaAtualizacaoEntrada entrada) {
        return ocorrencias.atualizar(id, entrada);
    }

    @DeleteMapping("/api/ocorrencias/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        ocorrencias.remover(id);
        return ResponseEntity.noContent().build();
    }
}

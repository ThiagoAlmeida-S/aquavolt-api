package com.aquavolt.aquavolt_backend.controller;

import com.aquavolt.aquavolt_backend.model.Falha;
import com.aquavolt.aquavolt_backend.repository.FalhaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/falhas")
@RequiredArgsConstructor
public class FalhaController {

    private final FalhaRepository falhaRepository;

    // Registra uma nova falha (sempre começa como não resolvida)
    @PostMapping
    public ResponseEntity<Falha> registrar(@RequestBody Falha falha) {
        if (falha.getOrigem() == null || falha.getOrigem().isBlank()
                || falha.getDescricao() == null || falha.getDescricao().isBlank()
                || falha.getSeveridade() == null) {
            return ResponseEntity.badRequest().build();
        }
        falha.setId(null);
        falha.setResolvida(false);
        return ResponseEntity.status(HttpStatus.CREATED).body(falhaRepository.save(falha));
    }

    // Histórico completo
    @GetMapping
    public List<Falha> listar() {
        return falhaRepository.findAllByOrderByDataHoraDesc();
    }

    // Alertas ativos (não resolvidos)
    @GetMapping("/ativas")
    public List<Falha> ativas() {
        return falhaRepository.findByResolvidaFalseOrderByDataHoraDesc();
    }

    // Marca a falha como resolvida
    @PutMapping("/{id}/resolver")
    public ResponseEntity<Falha> resolver(@PathVariable Long id) {
        return falhaRepository.findById(id)
                .map(falha -> {
                    falha.setResolvida(true);
                    return ResponseEntity.ok(falhaRepository.save(falha));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
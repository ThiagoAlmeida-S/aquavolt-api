package com.aquavolt.aquavolt_backend.controller;

import com.aquavolt.aquavolt_backend.model.Atuador;
import com.aquavolt.aquavolt_backend.repository.AtuadorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/atuadores")
@RequiredArgsConstructor
public class AtuadorController {

    private final AtuadorRepository atuadorRepository;

    // Cadastra um atuador (sempre começa desligado)
    @PostMapping
    public ResponseEntity<Atuador> criar(@RequestBody Atuador atuador) {
        if (atuador.getNome() == null || atuador.getNome().isBlank() || atuador.getTipo() == null) {
            return ResponseEntity.badRequest().build();
        }
        atuador.setId(null);
        atuador.setLigado(false);
        return ResponseEntity.status(HttpStatus.CREATED).body(atuadorRepository.save(atuador));
    }

    // Lista todos os atuadores e seus estados
    @GetMapping
    public List<Atuador> listar() {
        return atuadorRepository.findAll();
    }

    // Liga o atuador
    @PutMapping("/{id}/ligar")
    public ResponseEntity<Atuador> ligar(@PathVariable Long id) {
        return alterarEstado(id, true);
    }

    // Desliga o atuador
    @PutMapping("/{id}/desligar")
    public ResponseEntity<Atuador> desligar(@PathVariable Long id) {
        return alterarEstado(id, false);
    }

    private ResponseEntity<Atuador> alterarEstado(Long id, boolean ligado) {
        return atuadorRepository.findById(id)
                .map(atuador -> {
                    atuador.setLigado(ligado);
                    return ResponseEntity.ok(atuadorRepository.save(atuador));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}

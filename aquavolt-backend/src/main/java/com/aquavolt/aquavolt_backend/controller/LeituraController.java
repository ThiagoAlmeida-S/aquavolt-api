package com.aquavolt.aquavolt_backend.controller;

import com.aquavolt.aquavolt_backend.model.Leitura;
import com.aquavolt.aquavolt_backend.model.TipoLeitura;
import com.aquavolt.aquavolt_backend.repository.LeituraRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/leituras")
@RequiredArgsConstructor
public class LeituraController {

    private final LeituraRepository leituraRepository;

    // Registra uma nova leitura
    @PostMapping
    public ResponseEntity<Leitura> registrar(@RequestBody Leitura leitura) {
        if (leitura.getTipo() == null || leitura.getValor() == null) {
            return ResponseEntity.badRequest().build();
        }
        leitura.setId(null); // garante que sempre cria uma nova leitura
        Leitura salva = leituraRepository.save(leitura);
        return ResponseEntity.status(HttpStatus.CREATED).body(salva);
    }

    // Lista o histórico completo
    @GetMapping
    public List<Leitura> listar() {
        return leituraRepository.findAllByOrderByDataHoraDesc();
    }

    // Última leitura de cada tipo (nível, vazão e consumo)
    @GetMapping("/ultimas")
    public List<Leitura> ultimas() {
        List<Leitura> resultado = new ArrayList<>();
        for (TipoLeitura tipo : TipoLeitura.values()) {
            leituraRepository.findFirstByTipoOrderByDataHoraDesc(tipo)
                    .ifPresent(resultado::add);
        }
        return resultado;
    }
}

package com.aquavolt.aquavolt_backend.service;

import com.aquavolt.aquavolt_backend.model.Falha;
import com.aquavolt.aquavolt_backend.model.Leitura;
import com.aquavolt.aquavolt_backend.model.SeveridadeFalha;
import com.aquavolt.aquavolt_backend.model.TipoAtuador;
import com.aquavolt.aquavolt_backend.model.TipoLeitura;
import com.aquavolt.aquavolt_backend.repository.AtuadorRepository;
import com.aquavolt.aquavolt_backend.repository.FalhaRepository;
import com.aquavolt.aquavolt_backend.repository.LeituraRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class SimuladorLeituras {

    private static final String ORIGEM_RESERVATORIO = "Reservatório";
    private static final double NIVEL_CRITICO = 20.0;   // abre o alerta abaixo disso
    private static final double NIVEL_NORMAL = 30.0;    // fecha o alerta a partir disso

    private final LeituraRepository leituraRepository;
    private final AtuadorRepository atuadorRepository;
    private final FalhaRepository falhaRepository;
    private final Random random = new Random();

    // Executa a cada 5 segundos
    @Scheduled(fixedRate = 5000)
    public void gerar() {
        boolean bombaLigada = atuadorRepository.findAll().stream()
                .anyMatch(a -> a.getTipo() == TipoAtuador.BOMBA && Boolean.TRUE.equals(a.getLigado()));

        // Parte do último nível salvo, então não "pula" ao reiniciar
        double nivel = leituraRepository.findFirstByTipoOrderByDataHoraDesc(TipoLeitura.NIVEL_AGUA)
                .map(Leitura::getValor)
                .orElse(60.0);

        // Bomba ligada: nível sobe. Desligada: desce devagar.
        double variacao = bombaLigada
                ? 1.5 + random.nextDouble()
                : -(0.3 + random.nextDouble() * 0.7);
        nivel = Math.max(5, Math.min(100, nivel + variacao));

        double vazao = bombaLigada ? 18 + random.nextDouble() * 8 : 2 + random.nextDouble() * 3;
        double consumo = bombaLigada ? 3.5 + random.nextDouble() * 1.5 : 0.6 + random.nextDouble() * 0.6;

        salvar(TipoLeitura.NIVEL_AGUA, nivel);
        salvar(TipoLeitura.VAZAO, vazao);
        salvar(TipoLeitura.CONSUMO_ELETRICO, consumo);

        verificarAlertaDeNivel(nivel);
    }

    private void verificarAlertaDeNivel(double nivel) {
        List<Falha> abertas = falhaRepository.findByOrigemAndResolvidaFalse(ORIGEM_RESERVATORIO);

        if (nivel < NIVEL_CRITICO && abertas.isEmpty()) {
            // Abre um alerta novo (só um por vez)
            falhaRepository.save(new Falha(
                    null,
                    ORIGEM_RESERVATORIO,
                    "Nível de água crítico: abaixo de 20%",
                    SeveridadeFalha.CRITICA,
                    false,
                    null));
        } else if (nivel >= NIVEL_NORMAL && !abertas.isEmpty()) {
            // Nível se recuperou: fecha o alerta automaticamente
            abertas.forEach(falha -> falha.setResolvida(true));
            falhaRepository.saveAll(abertas);
        }
    }

    private void salvar(TipoLeitura tipo, double valor) {
        double arredondado = Math.round(valor * 10.0) / 10.0;
        leituraRepository.save(new Leitura(null, tipo, arredondado, null));
    }
}
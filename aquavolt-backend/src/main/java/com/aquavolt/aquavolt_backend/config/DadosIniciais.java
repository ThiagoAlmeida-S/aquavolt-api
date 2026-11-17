package com.aquavolt.aquavolt_backend.config;

import com.aquavolt.aquavolt_backend.model.Atuador;
import com.aquavolt.aquavolt_backend.model.TipoAtuador;
import com.aquavolt.aquavolt_backend.repository.AtuadorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DadosIniciais {

    // Roda uma vez ao iniciar a aplicação.
    // Se não houver nenhum atuador no banco, cadastra os três padrão.
    @Bean
    CommandLineRunner cadastrarAtuadoresPadrao(AtuadorRepository atuadorRepository) {
        return args -> {
            if (atuadorRepository.count() == 0) {
                atuadorRepository.saveAll(List.of(
                        new Atuador(null, "Bomba Principal", TipoAtuador.BOMBA, false, null),
                        new Atuador(null, "Válvula de Entrada", TipoAtuador.VALVULA, false, null),
                        new Atuador(null, "Relé do Painel Solar", TipoAtuador.RELE, false, null)
                ));
            }
        };
    }
}
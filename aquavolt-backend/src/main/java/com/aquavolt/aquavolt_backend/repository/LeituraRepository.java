package com.aquavolt.aquavolt_backend.repository;

import com.aquavolt.aquavolt_backend.model.Leitura;
import com.aquavolt.aquavolt_backend.model.TipoLeitura;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LeituraRepository extends JpaRepository<Leitura, Long> {

    // Todas as leituras, da mais recente para a mais antiga
    List<Leitura> findAllByOrderByDataHoraDesc();

    // Última leitura de um tipo (usaremos no painel do app)
    Optional<Leitura> findFirstByTipoOrderByDataHoraDesc(TipoLeitura tipo);
}
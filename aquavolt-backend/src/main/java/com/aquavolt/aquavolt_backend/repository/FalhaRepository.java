package com.aquavolt.aquavolt_backend.repository;

import com.aquavolt.aquavolt_backend.model.Falha;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FalhaRepository extends JpaRepository<Falha, Long> {

    // Histórico completo, da mais recente para a mais antiga
    List<Falha> findAllByOrderByDataHoraDesc();

    // Só as falhas que ainda não foram resolvidas (alertas ativos)
    List<Falha> findByResolvidaFalseOrderByDataHoraDesc();

    // Falhas ainda abertas de uma origem específica (evita alerta duplicado)
    List<Falha> findByOrigemAndResolvidaFalse(String origem);
}
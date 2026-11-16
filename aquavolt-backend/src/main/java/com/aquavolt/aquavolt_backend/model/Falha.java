package com.aquavolt.aquavolt_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "falhas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Falha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Onde a falha aconteceu (ex.: "Bomba Principal", "Sensor de Nível")
    @Column(nullable = false)
    private String origem;

    @Column(nullable = false)
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SeveridadeFalha severidade;

    // Boolean (com B maiúsculo) pelo mesmo motivo do bug do Atuador
    @Column(nullable = false)
    private Boolean resolvida = false;

    private LocalDateTime dataHora;

    @PrePersist
    void definirDataHora() {
        if (dataHora == null) {
            dataHora = LocalDateTime.now();
        }
    }
}
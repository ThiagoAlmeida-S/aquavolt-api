package com.aquavolt.aquavolt_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "atuadores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Atuador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoAtuador tipo;

    @Column(nullable = false)
    private Boolean ligado = false;

    private LocalDateTime atualizadoEm;

    // Atualiza a data sempre que o atuador for criado ou alterado
    @PrePersist
    @PreUpdate
    void registrarAtualizacao() {
        atualizadoEm = LocalDateTime.now();
    }
}
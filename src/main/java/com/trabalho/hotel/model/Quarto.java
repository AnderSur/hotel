package com.trabalho.hotel.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "quarto")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Quarto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Número do quarto é obrigatório")
    @Positive
    @Column(nullable = false, unique = true)
    private Integer numero;

    @NotNull(message = "Andar é obrigatório")
    @Column(nullable = false)
    private Integer andar;

    @Column(length = 255)
    private String descricao;

    @Column(nullable = false)
    private Boolean disponivel = true;

    @NotNull(message = "Tipo do quarto é obrigatório")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tipo_quarto_id", nullable = false)
    private TipoQuarto tipoQuarto;

    @OneToMany(mappedBy = "quarto", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Reserva> reservas;

    @OneToMany(mappedBy = "quarto", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Hospedagem> hospedagens;
}

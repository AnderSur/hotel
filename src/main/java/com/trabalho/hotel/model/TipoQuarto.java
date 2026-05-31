package com.trabalho.hotel.model;

import java.math.BigDecimal;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "tipo_quarto")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoQuarto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nome;
    private String descricao;

    @NotNull(message = "O preço por diária é obrigatório")
    @Positive(message = "O preço por diária deve ser um valor positivo")
    @Column(name = "preco_por_dia", nullable = false, precision = 10, scale = 2)
    private BigDecimal precoPorDia;

    @OneToMany(mappedBy = "tipoQuarto", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Quarto> quartos;

}

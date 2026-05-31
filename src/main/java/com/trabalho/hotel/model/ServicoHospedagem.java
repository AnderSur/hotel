package com.trabalho.hotel.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// S - Single Responsibility: tabela associativa entre Hospedagem e Servico
@Entity
@Table(name = "servico_hospedagem")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServicoHospedagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_solicitacao", nullable = false)
    private LocalDateTime dataSolicitacao = LocalDateTime.now();

    @Positive(message = "Quantidade deve ser positiva")
    @Column(nullable = false)
    private Integer quantidade = 1;

    @NotNull(message = "Hospedagem é obrigatória")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "hospedagem_id", nullable = false)
    private Hospedagem hospedagem;

    @NotNull(message = "Serviço é obrigatório")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "servico_id", nullable = false)
    private Servico servico;

    // O - Open/Closed: cálculo do subtotal sem alterar Servico ou Hospedagem
    public BigDecimal calcularSubtotal() {
        if (servico == null) return BigDecimal.ZERO;
        return servico.getValor().multiply(BigDecimal.valueOf(quantidade));
    }
}

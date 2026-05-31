package com.trabalho.hotel.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

// S - Single Responsibility: representa apenas a ocupação real do quarto
@Entity
@Table(name = "hospedagem")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Hospedagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "data_checkin", nullable = false)
    private LocalDate dataCheckin;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "data_checkout")
    private LocalDate dataCheckout;

    @Column(name = "quantidade_dias", nullable = false)
    private Integer quantidadeDias;

    // false = checkout realizado
    @Column(nullable = false)
    private Boolean ativa = true;

    @NotNull(message = "Cliente é obrigatório")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @NotNull(message = "Quarto é obrigatório")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "quarto_id", nullable = false)
    private Quarto quarto;

    // O - Open/Closed: relacionamento com serviços sem alterar a entidade hospedagem
    @OneToMany(mappedBy = "hospedagem", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<ServicoHospedagem> servicosHospedagem;

    // O - Open/Closed: cálculo total extensível (diárias + serviços)
    public BigDecimal calcularTotalDiarias() {
        if (quarto == null || quarto.getTipoQuarto() == null) return BigDecimal.ZERO;
        return quarto.getTipoQuarto().getPrecoPorDia()
                .multiply(BigDecimal.valueOf(quantidadeDias));
    }

    public BigDecimal calcularTotalServicos() {
        if (servicosHospedagem == null) return BigDecimal.ZERO;
        return servicosHospedagem.stream()
                .map(ServicoHospedagem::calcularSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calcularTotal() {
        return calcularTotalDiarias().add(calcularTotalServicos());
    }
}
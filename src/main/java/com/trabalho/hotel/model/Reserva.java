package com.trabalho.hotel.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reserva")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reserva {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Data de check-in é obrigatória")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "data_checkin", nullable = false)
    private LocalDate dataCheckin;

    @NotNull(message = "Data de check-out é obrigatória")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "data_checkout", nullable = false)
    private LocalDate dataCheckout;

    
    @Column(name = "quantidade_dias", nullable = false)
    private Integer quantidadeDias;

    @Column(name = "data_criacao", nullable = false)
    private LocalDate dataCriacao = LocalDate.now();

    @NotNull(message = "Cliente é obrigatório")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @NotNull(message = "Quarto é obrigatório")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "quarto_id", nullable = false)
    private Quarto quarto;

    // O - Open/Closed: método de cálculo extensível sem alterar a entidade
    public BigDecimal calcularTotal() {
        if (quarto == null || quarto.getTipoQuarto() == null) return BigDecimal.ZERO;
        return quarto.getTipoQuarto().getPrecoPorDia()
                .multiply(BigDecimal.valueOf(quantidadeDias));
    }

}

package com.trabalho.hotel.repository;

import com.trabalho.hotel.model.Quarto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface QuartoRepository extends JpaRepository<Quarto, Long> {

    @Query(value = "SELECT * FROM fn_quartos_disponiveis(:dataCheckin, :quantidadeDias)", nativeQuery = true)
    List<Object[]> findQuartosDisponiveisPorPeriodo(
        @Param("dataCheckin") LocalDate dataCheckin,
        @Param("quantidadeDias") Integer quantidadeDias);

    List<Quarto> findByDisponivelTrue();

    boolean existsByNumero(Integer numero);
}

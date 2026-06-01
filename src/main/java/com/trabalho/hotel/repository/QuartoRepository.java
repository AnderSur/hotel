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

    List<Quarto> findByDisponivelTrue();

    @Query(value = "SELECT  f.id_quarto, f.numero,  f.andar, " +
            "tq.descricao, tq.nome, tq.preco_por_dia " +
            "FROM fn_quartos_disponiveis(:dataCheckin, :quantidadeDias) f, tipo_quarto tq, quarto q " +
            "WHERE q.id = f.id_quarto AND tq.id = q.tipo_quarto_id", nativeQuery = true)
    List<Object[]> findQuartosDisponiveisPorPeriodo(
    @Param("dataCheckin") LocalDate dataCheckin,
    @Param("quantidadeDias") Integer quantidadeDias);

    boolean existsByNumero(Integer numero);
}

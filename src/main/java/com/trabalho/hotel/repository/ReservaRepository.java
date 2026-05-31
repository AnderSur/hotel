package com.trabalho.hotel.repository;

import com.trabalho.hotel.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    List<Reserva> findByClienteId(Long clienteId);

    List<Reserva> findByQuartoId(Long quartoId);

    /** Reservas com check-in em determinado dia (para relatório). */
    @Query("SELECT r FROM Reserva r WHERE r.dataCheckin = :data ORDER BY r.dataCheckin")
    List<Reserva> findByDataCheckin(@Param("data") LocalDate data);
    Optional<Reserva> findByClienteIdAndQuartoId(Long clienteId, Long quartoId);

    @Query("SELECT r FROM Reserva r WHERE r.quarto.id = :quartoId " +
        "AND r.dataCheckin < :checkout AND r.dataCheckout > :checkin")
    List<Reserva> findConflitos(@Param("quartoId") Long quartoId,
                                @Param("checkin") LocalDate checkin,
                                @Param("checkout") LocalDate checkout);
}
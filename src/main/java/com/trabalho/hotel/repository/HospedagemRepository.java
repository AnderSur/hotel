package com.trabalho.hotel.repository;

import com.trabalho.hotel.model.Hospedagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HospedagemRepository extends JpaRepository<Hospedagem, Long> {

    List<Hospedagem> findByAtivaTrue();

    List<Hospedagem> findByClienteId(Long clienteId);

    Optional<Hospedagem> findByQuartoIdAndAtivaTrue(Long quartoId);
}

package com.trabalho.hotel.repository;

import com.trabalho.hotel.model.Servico;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServicoRepository extends JpaRepository<Servico, Long> {
    Optional<Servico> findByIdentificadorInterno(String identificadorInterno);
}

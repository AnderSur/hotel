package com.trabalho.hotel.repository;

import com.trabalho.hotel.model.ServicoHospedagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServicoHospedagemRepository extends JpaRepository<ServicoHospedagem, Long> {
    List<ServicoHospedagem> findByHospedagemId(Long hospedagemId);
}

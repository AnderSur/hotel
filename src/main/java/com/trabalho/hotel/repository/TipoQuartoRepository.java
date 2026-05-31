package com.trabalho.hotel.repository;

import com.trabalho.hotel.model.TipoQuarto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoQuartoRepository extends JpaRepository<TipoQuarto, Long> {
}

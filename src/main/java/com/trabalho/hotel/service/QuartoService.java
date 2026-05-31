package com.trabalho.hotel.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trabalho.hotel.model.Quarto;
import com.trabalho.hotel.repository.QuartoRepository;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class QuartoService {
    private final QuartoRepository quartoRepository;

    public List<Quarto> listarTodos() {
        return quartoRepository.findAll();
    }

    public List<Quarto> listarDisponiveis() {
        return quartoRepository.findByDisponivelTrue();
    }

    // Chama a UDF com cursor do banco para validar disponibilidade no período
    public List<Object[]> listarDisponiveisPorPeriodo(LocalDate dataCheckin, Integer quantidadeDias) {
        return quartoRepository.findQuartosDisponiveisPorPeriodo(dataCheckin, quantidadeDias);
    }

    public Quarto buscarPorId(Long id) {
        return quartoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quarto não encontrado: " + id));
    }

    public Quarto salvar(Quarto quarto) {
        if (quarto.getId() == null && quartoRepository.existsByNumero(quarto.getNumero())) {
            throw new RuntimeException("Número de quarto já cadastrado: " + quarto.getNumero());
        }
        return quartoRepository.save(quarto);
    }

    public void deletar(Long id) {
        quartoRepository.deleteById(id);
    }
}

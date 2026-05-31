package com.trabalho.hotel.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trabalho.hotel.model.TipoQuarto;
import com.trabalho.hotel.repository.TipoQuartoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class TipoQuartoService {
    
    private final TipoQuartoRepository tipoQuartoRepository;

    public List<TipoQuarto> listarTodos() {
        return tipoQuartoRepository.findAll();
    }

    public TipoQuarto buscarPorId(Long id) {
        return tipoQuartoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tipo de quarto não encontrado: " + id));
    }

    public TipoQuarto salvar(TipoQuarto tipoQuarto) {
        return tipoQuartoRepository.save(tipoQuarto);
    }

    public void deletar(Long id) {
        tipoQuartoRepository.deleteById(id);
    }

}

package com.trabalho.hotel.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trabalho.hotel.model.Hospedagem;
import com.trabalho.hotel.model.Servico;
import com.trabalho.hotel.model.ServicoHospedagem;
import com.trabalho.hotel.repository.ServicoHospedagemRepository;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class ServicoHospedagemService {
    
    private final ServicoHospedagemRepository servicoHospedagemRepository;
    private final HospedagemService hospedagemService;
    private final ServicoService servicoService;

    public List<ServicoHospedagem> listarPorHospedagem(Long hospedagemId) {
        return servicoHospedagemRepository.findByHospedagemId(hospedagemId);
    }

    public ServicoHospedagem solicitarServico(Long hospedagemId, Long servicoId, Integer quantidade) {
        Hospedagem hospedagem = hospedagemService.buscarPorId(hospedagemId);
        if (!hospedagem.getAtiva()) {
            throw new RuntimeException("Não é possível solicitar serviços em hospedagem encerrada.");
        }
        Servico servico = servicoService.buscarPorId(servicoId);

        ServicoHospedagem sh = ServicoHospedagem.builder()
                .hospedagem(hospedagem)
                .servico(servico)
                .quantidade(quantidade)
                .dataSolicitacao(LocalDateTime.now())
                .build();

        return servicoHospedagemRepository.save(sh);
    }

    public void deletar(Long id) {
        servicoHospedagemRepository.deleteById(id);
    }
}

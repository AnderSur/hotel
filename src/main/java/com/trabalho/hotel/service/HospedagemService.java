package com.trabalho.hotel.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trabalho.hotel.model.Hospedagem;
import com.trabalho.hotel.model.Quarto;
import com.trabalho.hotel.model.Reserva;
import com.trabalho.hotel.repository.HospedagemRepository;
import com.trabalho.hotel.repository.ReservaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class HospedagemService {
    
    private final HospedagemRepository hospedagemRepository;
    private final ReservaRepository reservaRepository;
    private final QuartoService quartoService;


    public List<Hospedagem> listarTodas() {
        return hospedagemRepository.findAll();
    }

    public List<Hospedagem> listarAtivas() {
        return hospedagemRepository.findByAtivaTrue();
    }

    public Hospedagem buscarPorId(Long id) {
        return hospedagemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hospedagem não encontrada: " + id));
    }

    // Checkin via reserva: remove reserva e ocupa quarto
    public Hospedagem realizarCheckinComReserva(Long reservaId) {
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new RuntimeException("Reserva não encontrada"));

        Hospedagem hospedagem = Hospedagem.builder()
                .cliente(reserva.getCliente())
                .quarto(reserva.getQuarto())
                .dataCheckin(LocalDate.now())
                .quantidadeDias(reserva.getQuantidadeDias())
                .ativa(true)
                .build();

        // Remove reserva após checkin (regra de negócio)
        reservaRepository.delete(reserva);

        // Marca quarto como indisponível
        Quarto quarto = reserva.getQuarto();
        quarto.setDisponivel(false);
        quartoService.salvar(quarto);

        return hospedagemRepository.save(hospedagem);
    }

    // Checkin direto sem reserva (sujeito à disponibilidade)
    public Hospedagem realizarCheckinDireto(Hospedagem hospedagem) {
        if (hospedagem.getQuantidadeDias() == null || hospedagem.getQuantidadeDias() <= 0) {
        throw new RuntimeException("Quantidade de dias inválida.");
    }
        Quarto quarto = quartoService.buscarPorId(hospedagem.getQuarto().getId());
        if (!quarto.getDisponivel()) {
            throw new RuntimeException("Quarto indisponível para check-in direto.");
        }
        quarto.setDisponivel(false);
        quartoService.salvar(quarto);
        hospedagem.setDataCheckin(LocalDate.now());
        hospedagem.setAtiva(true);
        return hospedagemRepository.save(hospedagem);
    }

    // Checkout: libera quarto e encerra hospedagem
    public Hospedagem realizarCheckout(Long hospedagemId) {
        Hospedagem hospedagem = buscarPorId(hospedagemId);
        hospedagem.setAtiva(false);
        hospedagem.setDataCheckout(LocalDate.now());

        long dias = ChronoUnit.DAYS.between(hospedagem.getDataCheckin(), LocalDate.now());
        hospedagem.setQuantidadeDias((int) dias);

        Quarto quarto = hospedagem.getQuarto();
        quarto.setDisponivel(true);
        quartoService.salvar(quarto);

        return hospedagemRepository.save(hospedagem);
    }

    public void deletar(Long id) {
        hospedagemRepository.deleteById(id);
    }
}

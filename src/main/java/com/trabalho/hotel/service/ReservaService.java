package com.trabalho.hotel.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trabalho.hotel.model.Reserva;
import com.trabalho.hotel.repository.ReservaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservaService {
    
    private final ReservaRepository reservaRepository;
    
    public List<Reserva> listarTodas() {
        return reservaRepository.findAll();
    }

    public List<Reserva> listarPorDia(LocalDate data) {
        return reservaRepository.findByDataCheckin(data);
    }

    public Reserva buscarPorId(Long id) {
        return reservaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva não encontrada: " + id));
    }

    public Reserva salvar(Reserva reserva) {
        // Calcula dias automaticamente
        long dias = ChronoUnit.DAYS.between(reserva.getDataCheckin(), reserva.getDataCheckout());
        reserva.setQuantidadeDias((int) dias);

        // Valida conflito de período
        List<Reserva> conflitos = reservaRepository.findConflitos(
                reserva.getQuarto().getId(),
                reserva.getDataCheckin(),
                reserva.getDataCheckout());

        if (!conflitos.isEmpty()) {
            throw new RuntimeException("Quarto já reservado neste período.");
        }
        return reservaRepository.save(reserva);
    }

    public void deletar(Long id) {
        reservaRepository.deleteById(id);
    }

}

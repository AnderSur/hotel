package com.trabalho.hotel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.trabalho.hotel.repository.ClienteRepository;
import com.trabalho.hotel.repository.HospedagemRepository;
import com.trabalho.hotel.repository.QuartoRepository;
import com.trabalho.hotel.repository.ReservaRepository;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class IndexController {
    
    private final ClienteRepository clienteRepository;
    private final QuartoRepository quartoRepository;
    private final ReservaRepository reservaRepository;
    private final HospedagemRepository hospedagemRepository;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("totalClientes",      clienteRepository.count());
        model.addAttribute("totalQuartos",       quartoRepository.count());
        model.addAttribute("quartosDisponiveis", quartoRepository.findByDisponivelTrue().size());
        
        model.addAttribute("totalReservas",      reservaRepository.count());
        model.addAttribute("hospedagensAtivas",  hospedagemRepository.findByAtivaTrue().size());
        model.addAttribute("hospedagens",        hospedagemRepository.findByAtivaTrue());
        return "index";
    }

}

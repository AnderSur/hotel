package com.trabalho.hotel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.trabalho.hotel.model.Hospedagem;
import com.trabalho.hotel.service.ClienteService;
import com.trabalho.hotel.service.HospedagemService;
import com.trabalho.hotel.service.QuartoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/hospedagens")
@RequiredArgsConstructor
public class HospegemController {
    
    private final HospedagemService hospedagemService;
    private final ClienteService clienteService;
    private final QuartoService quartoService;
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("hospedagens", hospedagemService.listarAtivas());
        return "hospedagem/lista";
    }

    // Checkin com reserva existente
    @GetMapping("/checkin-reserva/{reservaId}")
    public String checkinComReserva(@PathVariable Long reservaId) {
        hospedagemService.realizarCheckinComReserva(reservaId);
        return "redirect:/hospedagens";
    }

    @GetMapping("/checkin-direto")
    public String checkinDiretoForm(Model model) {
        model.addAttribute("hospedagem", new Hospedagem());
        model.addAttribute("clientes", clienteService.listarTodos());
        model.addAttribute("quartos", quartoService.listarDisponiveis());
        return "hospedagem/checkin-direto";
    }

    @PostMapping("/checkin-direto")
    public String checkinDireto(@Valid @ModelAttribute Hospedagem hospedagem,
                                BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("clientes", clienteService.listarTodos());
            model.addAttribute("quartos", quartoService.listarDisponiveis());
            return "hospedagem/checkin-direto";
        }
        hospedagemService.realizarCheckinDireto(hospedagem);
        return "redirect:/hospedagens";
    }

    @GetMapping("/checkout/{id}")
    public String checkout(@PathVariable Long id) {
        hospedagemService.realizarCheckout(id);
        return "redirect:/hospedagens";
    }

    @GetMapping("/{id}")
    public String detalhe(@PathVariable Long id, Model model) {
        model.addAttribute("hospedagem", hospedagemService.buscarPorId(id));
        return "hospedagem/detalhe";
    }
}

package com.trabalho.hotel.controller;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.trabalho.hotel.model.Reserva;
import com.trabalho.hotel.service.ClienteService;
import com.trabalho.hotel.service.QuartoService;
import com.trabalho.hotel.service.ReservaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/reservas")
@RequiredArgsConstructor
public class ReservaController {
    
    private final ReservaService reservaService;
    private final ClienteService clienteService;
    private final QuartoService quartoService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("reservas", reservaService.listarTodas());
        return "reserva/lista";
    }

    // Relatório do dia (requisito do trabalho)
    @GetMapping("/relatorio-dia")
    public String relatorioDia(@RequestParam(required = false)
                               @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
                               Model model) {
        if (data != null) {
            model.addAttribute("reservas", reservaService.listarPorDia(data));
            model.addAttribute("data", data);
        }
        return "reserva/relatorio-dia";
    }

    @GetMapping("/novo")
    public String novoForm(Model model) {
        model.addAttribute("reserva", new Reserva());
        model.addAttribute("clientes", clienteService.listarTodos());
        model.addAttribute("quartos", quartoService.listarDisponiveis());
        return "reserva/form";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute Reserva reserva, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("clientes", clienteService.listarTodos());
            model.addAttribute("quartos", quartoService.listarDisponiveis());
            return "reserva/form";
        }
        reservaService.salvar(reserva);
        return "redirect:/reservas";
    }

    @GetMapping("/deletar/{id}")
    public String deletar(@PathVariable Long id) {
        reservaService.deletar(id);
        return "redirect:/reservas";
    }
}

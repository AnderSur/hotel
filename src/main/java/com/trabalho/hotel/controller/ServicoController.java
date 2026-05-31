package com.trabalho.hotel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.trabalho.hotel.model.Servico;
import com.trabalho.hotel.service.ServicoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@Controller
@RequestMapping("/servicos")
@RequiredArgsConstructor
public class ServicoController {
    
    private final ServicoService servicoService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("servicos", servicoService.listarTodos());
        return "servico/lista";
    }

    @GetMapping("/novo")
    public String novoForm(Model model) {
        model.addAttribute("servico", new Servico());
        return "servico/form";
    }
    
    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute Servico servico, BindingResult result) {
        if (result.hasErrors()) return "servico/form";
        servicoService.salvar(servico);
        return "redirect:/servicos";
    }

    @GetMapping("/editar/{id}")
    public String editarForm(@PathVariable Long id, Model model) {
        model.addAttribute("servico", servicoService.buscarPorId(id));
        return "servico/form";
    }

    @GetMapping("/deletar/{id}")
    public String deletar(@PathVariable Long id) {
        servicoService.deletar(id);
        return "redirect:/servicos";
    }
}

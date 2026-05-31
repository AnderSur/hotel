package com.trabalho.hotel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.trabalho.hotel.model.TipoQuarto;
import com.trabalho.hotel.service.TipoQuartoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/tipos-quarto")
@RequiredArgsConstructor
public class TipoQuartoController {
    
    private final TipoQuartoService tipoQuartoService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("tipos", tipoQuartoService.listarTodos());
        return "tipoquarto/lista";
    }

    @GetMapping("/novo")
    public String novoForm(Model model) {
        model.addAttribute("tipoQuarto", new TipoQuarto());
        return "tipoquarto/form";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute TipoQuarto tipoQuarto, BindingResult result) {
        if (result.hasErrors()) return "tipoquarto/form";
        tipoQuartoService.salvar(tipoQuarto);
        return "redirect:/tipos-quarto";
    }

    @GetMapping("/editar/{id}")
    public String editarForm(@PathVariable Long id, Model model) {
        model.addAttribute("tipoQuarto", tipoQuartoService.buscarPorId(id));
        return "tipoquarto/form";
    }

    @GetMapping("/deletar/{id}")
    public String deletar(@PathVariable Long id) {
        tipoQuartoService.deletar(id);
        return "redirect:/tipos-quarto";
    }
}

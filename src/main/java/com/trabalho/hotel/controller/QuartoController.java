package com.trabalho.hotel.controller;

import java.time.LocalDate;
import java.util.List;

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

import com.trabalho.hotel.model.Quarto;
import com.trabalho.hotel.service.QuartoService;
import com.trabalho.hotel.service.TipoQuartoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/quartos")
@RequiredArgsConstructor
public class QuartoController {
    
    private final QuartoService quartoService;
    private final TipoQuartoService tipoQuartoService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("quartos", quartoService.listarTodos());
        return "quarto/lista";
    }

    // View de disponibilidade com UDF (requisito do trabalho)
    @GetMapping("/disponiveis")
    public String disponiveis(@RequestParam(required = false)
                              @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataCheckin,
                              @RequestParam(required = false) Integer quantidadeDias,
                              Model model) {
        if (dataCheckin != null && quantidadeDias != null) {
            try{
            List<Object[]> disponiveis = quartoService.listarDisponiveisPorPeriodo(dataCheckin, quantidadeDias);
            model.addAttribute("quartos", disponiveis);
            model.addAttribute("dataCheckin", dataCheckin);
            model.addAttribute("quantidadeDias", quantidadeDias);
            } catch (Exception e) {
                model.addAttribute("erro", 
                "Erro ao consultar disponibilidade. Verifique se a UDF está criada no banco. Detalhe: " 
                + e.getMessage());
            model.addAttribute("dataCheckin", dataCheckin);
            model.addAttribute("quantidadeDias", quantidadeDias);
            }
        }
        return "quarto/disponiveis";
    }

    @GetMapping("/novo")
    public String novoForm(Model model) {
        model.addAttribute("quarto", new Quarto());
        model.addAttribute("tipos", tipoQuartoService.listarTodos());
        return "quarto/form";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute Quarto quarto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("tipos", tipoQuartoService.listarTodos());
            return "quarto/form";
        }
        quartoService.salvar(quarto);
        return "redirect:/quartos";
    }

    @GetMapping("/editar/{id}")
    public String editarForm(@PathVariable Long id, Model model) {
        model.addAttribute("quarto", quartoService.buscarPorId(id));
        model.addAttribute("tipos", tipoQuartoService.listarTodos());
        return "quarto/form";
    }

    @GetMapping("/deletar/{id}")
    public String deletar(@PathVariable Long id) {
        quartoService.deletar(id);
        return "redirect:/quartos";
    }
}

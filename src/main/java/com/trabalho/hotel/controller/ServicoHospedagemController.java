package com.trabalho.hotel.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.trabalho.hotel.service.HospedagemService;
import com.trabalho.hotel.service.ServicoHospedagemService;
import com.trabalho.hotel.service.ServicoService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/servicos-hospedagem")
@RequiredArgsConstructor
public class ServicoHospedagemController {
    
    private final ServicoHospedagemService servicoHospedagemService;
    private final HospedagemService hospedagemService;
    private final ServicoService servicoService;

    public String listarPorHospedagem(@PathVariable Long hospedagemId, Model model) {
        model.addAttribute("servicos", servicoHospedagemService.listarPorHospedagem(hospedagemId));
        model.addAttribute("hospedagem", hospedagemService.buscarPorId(hospedagemId));
        model.addAttribute("catalogo", servicoService.listarTodos());
        return "servicohospedagem/lista";
    }

    @PostMapping("/solicitar")
    public String solicitar(@RequestParam Long hospedagemId,
                            @RequestParam Long servicoId,
                            @RequestParam Integer quantidade) {
        servicoHospedagemService.solicitarServico(hospedagemId, servicoId, quantidade);
        return "redirect:/servicos-hospedagem/hospedagem/" + hospedagemId;
    }

    @GetMapping("/deletar/{id}/{hospedagemId}")
    public String deletar(@PathVariable Long id, @PathVariable Long hospedagemId) {
        servicoHospedagemService.deletar(id);
        return "redirect:/servicos-hospedagem/hospedagem/" + hospedagemId;
    }
}

package com.trabalho.hotel.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(RuntimeException.class)
    public String handleError(RuntimeException ex, 
                              RedirectAttributes redirectAttributes) {
        // flash attribute sobrevive ao redirect
        redirectAttributes.addFlashAttribute("erro", ex.getMessage());
        return "redirect:/";
    }

}

package com.projeto.sistemaCantinhoLeitura.controler;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Controler {
	
    @GetMapping("/home")
    public String home() {
        return "gestao/home.html"; // Retorna o nome do arquivo HTML na pasta templates (src/main/resources/templates)
    }

}

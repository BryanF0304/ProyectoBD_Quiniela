package org.example.proyectobd_quiniela.controller;

import jakarta.servlet.http.HttpSession;
import org.example.proyectobd_quiniela.model.Quiniela;
import org.example.proyectobd_quiniela.model.UsuarioSesion;
import org.example.proyectobd_quiniela.service.QuinielaService;
import org.example.proyectobd_quiniela.service.RankingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RankingController {

    private final QuinielaService quinielaService;
    private final RankingService rankingService;

    public RankingController(QuinielaService quinielaService, RankingService rankingService) {
        this.quinielaService = quinielaService;
        this.rankingService = rankingService;
    }

    @GetMapping("/quinielas/{id}/ranking")
    public String ranking(@PathVariable int id, HttpSession session,
                          Model model, RedirectAttributes redirect) {
        Quiniela quiniela = quinielaService.buscarPorId(id);
        if (quiniela == null) {
            redirect.addFlashAttribute("error", "La quiniela no existe.");
            return "redirect:/quinielas";
        }
        UsuarioSesion usuario = (UsuarioSesion) session.getAttribute("usuario");
        model.addAttribute("quiniela", quiniela);
        model.addAttribute("ranking", rankingService.listarPorQuiniela(id));
        model.addAttribute("miId", usuario.getIdUsuario());
        return "ranking";
    }
}
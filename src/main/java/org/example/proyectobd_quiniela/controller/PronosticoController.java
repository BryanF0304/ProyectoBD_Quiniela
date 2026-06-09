package org.example.proyectobd_quiniela.controller;

import jakarta.servlet.http.HttpSession;
import org.example.proyectobd_quiniela.exception.NegocioException;
import org.example.proyectobd_quiniela.model.Quiniela;
import org.example.proyectobd_quiniela.model.UsuarioSesion;
import org.example.proyectobd_quiniela.service.PartidoService;
import org.example.proyectobd_quiniela.service.PronosticoService;
import org.example.proyectobd_quiniela.service.QuinielaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PronosticoController {

    private final QuinielaService quinielaService;
    private final PartidoService partidoService;
    private final PronosticoService pronosticoService;

    public PronosticoController(QuinielaService quinielaService,
                                PartidoService partidoService,
                                PronosticoService pronosticoService) {
        this.quinielaService = quinielaService;
        this.partidoService = partidoService;
        this.pronosticoService = pronosticoService;
    }

    @GetMapping("/quinielas/{id}/pronosticos")
    public String pagina(@PathVariable int id, HttpSession session,
                         Model model, RedirectAttributes redirect) {
        Quiniela quiniela = quinielaService.buscarPorId(id);
        if (quiniela == null) {
            redirect.addFlashAttribute("error", "La quiniela no existe.");
            return "redirect:/quinielas";
        }
        UsuarioSesion usuario = (UsuarioSesion) session.getAttribute("usuario");
        if (!quinielaService.estaInscrito(usuario.getIdUsuario(), id)) {
            redirect.addFlashAttribute("error", "Debes inscribirte en la quiniela para pronosticar.");
            return "redirect:/quinielas/" + id;
        }
        model.addAttribute("quiniela", quiniela);
        model.addAttribute("partidos", partidoService.listarPorQuiniela(id));
        model.addAttribute("misPronosticos", pronosticoService.misPronosticos(usuario.getIdUsuario(), id));
        return "pronosticos";
    }

    @PostMapping("/quinielas/{id}/pronosticos")
    public String registrar(@PathVariable int id,
                            @RequestParam int idPartido,
                            @RequestParam int golesLocal,
                            @RequestParam int golesVisita,
                            HttpSession session,
                            RedirectAttributes redirect) {
        UsuarioSesion usuario = (UsuarioSesion) session.getAttribute("usuario");
        try {
            pronosticoService.registrar(usuario.getIdUsuario(), idPartido, golesLocal, golesVisita);
            redirect.addFlashAttribute("exito", "Pronostico registrado.");
        } catch (NegocioException ex) {
            redirect.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/quinielas/" + id + "/pronosticos";
    }
}
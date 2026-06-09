package org.example.proyectobd_quiniela.controller;

import jakarta.servlet.http.HttpSession;
import org.example.proyectobd_quiniela.exception.NegocioException;
import org.example.proyectobd_quiniela.model.PartidoForm;
import org.example.proyectobd_quiniela.model.UsuarioSesion;
import org.example.proyectobd_quiniela.service.PartidoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/partidos")
public class PartidoController {

    private final PartidoService partidoService;

    public PartidoController(PartidoService partidoService) {
        this.partidoService = partidoService;
    }

    @GetMapping
    public String pagina(Model model) {
        if (!model.containsAttribute("partidoForm")) {
            model.addAttribute("partidoForm", new PartidoForm());
        }
        model.addAttribute("equipos", partidoService.listarEquipos());
        model.addAttribute("partidos", partidoService.listarTodos());
        return "partidos";
    }

    @PostMapping
    public String crear(@ModelAttribute PartidoForm partidoForm,
                        RedirectAttributes redirect) {
        try {
            partidoService.crear(partidoForm);
            redirect.addFlashAttribute("exito", "Partido registrado correctamente.");
        } catch (NegocioException ex) {
            redirect.addFlashAttribute("error", ex.getMessage());
            redirect.addFlashAttribute("partidoForm", partidoForm);
        }
        return "redirect:/admin/partidos";
    }

    @PostMapping("/{id}/resultado")
    public String registrarResultado(@PathVariable int id,
                                     @RequestParam int golesLocal,
                                     @RequestParam int golesVisita,
                                     HttpSession session,
                                     RedirectAttributes redirect) {
        UsuarioSesion usuario = (UsuarioSesion) session.getAttribute("usuario");
        try {
            partidoService.registrarResultado(id, golesLocal, golesVisita, usuario.getIdUsuario());
            redirect.addFlashAttribute("exito", "Resultado registrado y puntos calculados.");
        } catch (NegocioException ex) {
            redirect.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/admin/partidos";
    }
}
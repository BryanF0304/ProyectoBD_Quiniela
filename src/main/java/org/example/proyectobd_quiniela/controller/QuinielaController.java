package org.example.proyectobd_quiniela.controller;

import jakarta.servlet.http.HttpSession;
import org.example.proyectobd_quiniela.exception.NegocioException;
import org.example.proyectobd_quiniela.model.Quiniela;
import org.example.proyectobd_quiniela.model.QuinielaForm;
import org.example.proyectobd_quiniela.model.UsuarioSesion;
import org.example.proyectobd_quiniela.service.PartidoService;
import org.example.proyectobd_quiniela.service.QuinielaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class QuinielaController {

    private final QuinielaService quinielaService;
    private final PartidoService partidoService;

    public QuinielaController(QuinielaService quinielaService, PartidoService partidoService) {
        this.quinielaService = quinielaService;
        this.partidoService = partidoService;
    }

    @GetMapping("/quinielas")
    public String listar(Model model) {
        model.addAttribute("quinielas", quinielaService.listarTodas());
        return "quinielas";
    }

    @GetMapping("/quinielas/{id}")
    public String detalle(@PathVariable int id, HttpSession session,
                          Model model, RedirectAttributes redirect) {
        Quiniela quiniela = quinielaService.buscarPorId(id);
        if (quiniela == null) {
            redirect.addFlashAttribute("error", "La quiniela no existe.");
            return "redirect:/quinielas";
        }
        UsuarioSesion usuario = (UsuarioSesion) session.getAttribute("usuario");
        model.addAttribute("quiniela", quiniela);
        model.addAttribute("inscrito", quinielaService.estaInscrito(usuario.getIdUsuario(), id));
        model.addAttribute("participantes", quinielaService.listarParticipantes(id));
        model.addAttribute("partidos", partidoService.listarPorQuiniela(id));
        if (usuario.esAdministrador()) {
            model.addAttribute("disponibles", partidoService.listarDisponibles(id));
        }
        return "quiniela-detalle";
    }

    @PostMapping("/quinielas/{id}/inscribir")
    public String inscribir(@PathVariable int id,
                            @RequestParam(name = "aceptoReglas", defaultValue = "false") boolean aceptoReglas,
                            HttpSession session,
                            RedirectAttributes redirect) {
        UsuarioSesion usuario = (UsuarioSesion) session.getAttribute("usuario");
        try {
            quinielaService.inscribir(usuario.getIdUsuario(), id, aceptoReglas);
            redirect.addFlashAttribute("exito", "Te inscribiste correctamente.");
        } catch (NegocioException ex) {
            redirect.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/quinielas/" + id;
    }

    @PostMapping("/admin/quinielas/{id}/partidos")
    public String agregarPartido(@PathVariable int id,
                                 @RequestParam("idPartido") int idPartido,
                                 RedirectAttributes redirect) {
        try {
            quinielaService.agregarPartido(id, idPartido);
            redirect.addFlashAttribute("exito", "Partido agregado a la quiniela.");
        } catch (NegocioException ex) {
            redirect.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/quinielas/" + id;
    }

    @GetMapping("/admin/quinielas/nueva")
    public String mostrarFormulario(Model model) {
        if (!model.containsAttribute("quinielaForm")) {
            model.addAttribute("quinielaForm", new QuinielaForm());
        }
        return "quiniela-form";
    }

    @PostMapping("/admin/quinielas")
    public String crear(@ModelAttribute QuinielaForm quinielaForm,
                        HttpSession session,
                        RedirectAttributes redirect) {
        UsuarioSesion usuario = (UsuarioSesion) session.getAttribute("usuario");
        try {
            quinielaService.crear(quinielaForm, usuario.getIdUsuario());
            redirect.addFlashAttribute("exito", "Quiniela creada correctamente.");
            return "redirect:/quinielas";
        } catch (NegocioException ex) {
            redirect.addFlashAttribute("error", ex.getMessage());
            redirect.addFlashAttribute("quinielaForm", quinielaForm);
            return "redirect:/admin/quinielas/nueva";
        }
    }
}
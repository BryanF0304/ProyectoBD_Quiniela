package org.example.proyectobd_quiniela.controller;

import jakarta.servlet.http.HttpSession;
import org.example.proyectobd_quiniela.exception.NegocioException;
import org.example.proyectobd_quiniela.model.LoginForm;
import org.example.proyectobd_quiniela.model.RegistroForm;
import org.example.proyectobd_quiniela.model.UsuarioSesion;
import org.example.proyectobd_quiniela.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {
        if (!model.containsAttribute("registroForm")) {
            model.addAttribute("registroForm", new RegistroForm());
        }
        return "registro";
    }

    @PostMapping("/registro")
    public String procesarRegistro(@ModelAttribute RegistroForm registroForm,
                                   RedirectAttributes redirect) {
        try {
            usuarioService.registrar(registroForm);
            redirect.addFlashAttribute("exito",
                    "Cuenta creada correctamente. Ya puedes iniciar sesion.");
            return "redirect:/login";
        } catch (NegocioException ex) {
            redirect.addFlashAttribute("error", ex.getMessage());
            redirect.addFlashAttribute("registroForm", registroForm);
            return "redirect:/registro";
        }
    }

    @GetMapping("/login")
    public String mostrarLogin(Model model) {
        if (!model.containsAttribute("loginForm")) {
            model.addAttribute("loginForm", new LoginForm());
        }
        return "login";
    }

    @PostMapping("/login")
    public String procesarLogin(@ModelAttribute LoginForm loginForm,
                                HttpSession session,
                                RedirectAttributes redirect) {
        try {
            UsuarioSesion usuario = usuarioService.autenticar(
                    loginForm.getNombreUsuario(), loginForm.getContrasena());
            session.setAttribute("usuario", usuario);
            return "redirect:/";
        } catch (NegocioException ex) {
            redirect.addFlashAttribute("error", ex.getMessage());
            redirect.addFlashAttribute("loginForm", loginForm);
            return "redirect:/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
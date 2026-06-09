package org.example.proyectobd_quiniela.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.proyectobd_quiniela.model.UsuarioSesion;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class AuthInterceptor  implements HandlerInterceptor{
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        HttpSession session = request.getSession(false);
        UsuarioSesion usuario = session == null
                ? null
                : (UsuarioSesion) session.getAttribute("usuario");

        String ctx = request.getContextPath();
        String path = request.getRequestURI().substring(ctx.length());

        if (usuario == null) {
            response.sendRedirect(ctx + "/login?error="
                    + encode("Debes iniciar sesion para continuar."));
            return false;
        }

        if (path.startsWith("/admin") && !usuario.esAdministrador()) {
            response.sendRedirect(ctx + "/?error="
                    + encode("Acceso permitido solo para administradores."));
            return false;
        }

        return true;
    }

    private String encode(String texto) {
        return URLEncoder.encode(texto, StandardCharsets.UTF_8);
    }
}

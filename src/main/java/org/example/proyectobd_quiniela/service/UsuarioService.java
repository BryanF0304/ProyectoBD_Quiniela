package org.example.proyectobd_quiniela.service;

import org.example.proyectobd_quiniela.model.RegistroForm;
import org.example.proyectobd_quiniela.model.UsuarioSesion;
import org.example.proyectobd_quiniela.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public int registrar(RegistroForm form) {
        return usuarioRepository.registrar(form);
    }

    public UsuarioSesion autenticar(String nombreUsuario, String contrasena) {
        return usuarioRepository.autenticar(nombreUsuario, contrasena);
    }
}
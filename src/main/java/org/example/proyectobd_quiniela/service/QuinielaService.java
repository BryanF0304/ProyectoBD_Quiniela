package org.example.proyectobd_quiniela.service;

import org.example.proyectobd_quiniela.exception.NegocioException;
import org.example.proyectobd_quiniela.model.Quiniela;
import org.example.proyectobd_quiniela.model.QuinielaForm;
import org.example.proyectobd_quiniela.repository.QuinielaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuinielaService {

    private final QuinielaRepository quinielaRepository;

    public QuinielaService(QuinielaRepository quinielaRepository) {
        this.quinielaRepository = quinielaRepository;
    }

    public int crear(QuinielaForm form, int idAdmin) {
        if (form.getFechaInicio() != null && form.getFechaCierre() != null
                && form.getFechaCierre().isBefore(form.getFechaInicio())) {
            throw new NegocioException("La fecha de cierre no puede ser anterior a la de inicio.");
        }
        return quinielaRepository.crear(form, idAdmin);
    }

    public List<Quiniela> listarTodas() {
        return quinielaRepository.listarTodas();
    }

    public Quiniela buscarPorId(int id) {
        return quinielaRepository.buscarPorId(id);
    }

    public boolean estaInscrito(int idUsuario, int idQuiniela) {
        return quinielaRepository.estaInscrito(idUsuario, idQuiniela);
    }

    public List<String> listarParticipantes(int idQuiniela) {
        return quinielaRepository.listarParticipantes(idQuiniela);
    }

    public void inscribir(int idUsuario, int idQuiniela, boolean aceptoReglas) {
        quinielaRepository.inscribir(idUsuario, idQuiniela, aceptoReglas);
    }

    public void agregarPartido(int idQuiniela, int idPartido) {
        quinielaRepository.agregarPartido(idQuiniela, idPartido);
    }
}
package org.example.proyectobd_quiniela.service;

import org.example.proyectobd_quiniela.exception.NegocioException;
import org.example.proyectobd_quiniela.model.Equipo;
import org.example.proyectobd_quiniela.model.Partido;
import org.example.proyectobd_quiniela.model.PartidoForm;
import org.example.proyectobd_quiniela.repository.EquipoRepository;
import org.example.proyectobd_quiniela.repository.PartidoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PartidoService {

    private final PartidoRepository partidoRepository;
    private final EquipoRepository equipoRepository;

    public PartidoService(PartidoRepository partidoRepository, EquipoRepository equipoRepository) {
        this.partidoRepository = partidoRepository;
        this.equipoRepository = equipoRepository;
    }

    public int crear(PartidoForm form) {
        if (form.getIdEquipoLocal() != null
                && form.getIdEquipoLocal().equals(form.getIdEquipoVisita())) {
            throw new NegocioException("El equipo local y el visitante no pueden ser el mismo.");
        }
        return partidoRepository.crear(form);
    }

    public void registrarResultado(int idPartido, int golesLocal, int golesVisita, int idAdmin) {
        partidoRepository.registrarResultado(idPartido, golesLocal, golesVisita, idAdmin);
    }

    public List<Partido> listarTodos() {
        return partidoRepository.listarTodos();
    }

    public List<Partido> listarPorQuiniela(int idQuiniela) {
        return partidoRepository.listarPorQuiniela(idQuiniela);
    }

    public List<Partido> listarDisponibles(int idQuiniela) {
        return partidoRepository.listarDisponibles(idQuiniela);
    }

    public List<Equipo> listarEquipos() {
        return equipoRepository.listarTodos();
    }
}
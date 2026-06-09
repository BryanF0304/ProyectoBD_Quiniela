package org.example.proyectobd_quiniela.service;

import org.example.proyectobd_quiniela.model.Pronostico;
import org.example.proyectobd_quiniela.repository.PronosticoRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PronosticoService {

    private final PronosticoRepository pronosticoRepository;

    public PronosticoService(PronosticoRepository pronosticoRepository) {
        this.pronosticoRepository = pronosticoRepository;
    }

    public void registrar(int idUsuario, int idPartido, int golesLocal, int golesVisita) {
        pronosticoRepository.registrar(idUsuario, idPartido, golesLocal, golesVisita);
    }

    public Map<Integer, Pronostico> misPronosticos(int idUsuario, int idQuiniela) {
        Map<Integer, Pronostico> mapa = new HashMap<>();
        for (Pronostico p : pronosticoRepository.listarPorUsuarioYQuiniela(idUsuario, idQuiniela)) {
            mapa.put(p.getIdPartido(), p);
        }
        return mapa;
    }
}
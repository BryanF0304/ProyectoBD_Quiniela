package org.example.proyectobd_quiniela.service;

import org.example.proyectobd_quiniela.model.RankingEntry;
import org.example.proyectobd_quiniela.repository.RankingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RankingService {

    private final RankingRepository rankingRepository;

    public RankingService(RankingRepository rankingRepository) {
        this.rankingRepository = rankingRepository;
    }

    public List<RankingEntry> listarPorQuiniela(int idQuiniela) {
        return rankingRepository.listarPorQuiniela(idQuiniela);
    }
}
package org.example.proyectobd_quiniela.repository;

import org.example.proyectobd_quiniela.model.RankingEntry;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RankingRepository {

    private final JdbcTemplate jdbc;

    public RankingRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<RankingEntry> listarPorQuiniela(int idQuiniela) {
        return jdbc.query(
                "SELECT id_usuario, nombre_usuario, puntaje_total, partidos_pronosticados, posicion "
                        + "FROM dbo.vw_RankingQuiniela WHERE id_quiniela = ? "
                        + "ORDER BY posicion, nombre_usuario",
                (rs, n) -> {
                    RankingEntry e = new RankingEntry();
                    e.setIdUsuario(rs.getInt("id_usuario"));
                    e.setNombreUsuario(rs.getString("nombre_usuario"));
                    e.setPuntajeTotal(rs.getInt("puntaje_total"));
                    e.setPartidosPronosticados(rs.getInt("partidos_pronosticados"));
                    e.setPosicion(rs.getInt("posicion"));
                    return e;
                }, idQuiniela);
    }
}
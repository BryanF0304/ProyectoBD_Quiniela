package org.example.proyectobd_quiniela.repository;

import org.example.proyectobd_quiniela.model.Equipo;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EquipoRepository {

    private final JdbcTemplate jdbc;

    public EquipoRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<Equipo> listarTodos() {
        return jdbc.query(
                "SELECT id_equipo, nombre, pais FROM dbo.Equipo ORDER BY nombre",
                (rs, n) -> {
                    Equipo e = new Equipo();
                    e.setIdEquipo(rs.getInt("id_equipo"));
                    e.setNombre(rs.getString("nombre"));
                    e.setPais(rs.getString("pais"));
                    return e;
                });
    }
}
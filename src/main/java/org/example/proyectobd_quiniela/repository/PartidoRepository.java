package org.example.proyectobd_quiniela.repository;

import org.example.proyectobd_quiniela.exception.NegocioException;
import org.example.proyectobd_quiniela.model.Partido;
import org.example.proyectobd_quiniela.model.PartidoForm;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class PartidoRepository {

    private static final String SELECT_BASE =
            "SELECT p.id_partido, el.nombre AS local, ev.nombre AS visita, p.fecha_hora, "
                    + "p.estado, p.goles_local, p.goles_visita FROM dbo.Partido p "
                    + "JOIN dbo.Equipo el ON el.id_equipo = p.id_equipo_local "
                    + "JOIN dbo.Equipo ev ON ev.id_equipo = p.id_equipo_visita ";

    private final JdbcTemplate jdbc;
    private final SimpleJdbcInsert insertPartido;
    private final SimpleJdbcCall registrarResultadoCall;

    public PartidoRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
        this.insertPartido = new SimpleJdbcInsert(jdbc)
                .withSchemaName("dbo")
                .withTableName("Partido")
                .usingColumns("id_equipo_local", "id_equipo_visita", "fecha_hora")
                .usingGeneratedKeyColumns("id_partido");

        this.registrarResultadoCall = new SimpleJdbcCall(jdbc)
                .withSchemaName("dbo")
                .withProcedureName("sp_RegistrarResultado")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("id_partido", Types.INTEGER),
                        new SqlParameter("goles_local", Types.INTEGER),
                        new SqlParameter("goles_visita", Types.INTEGER),
                        new SqlParameter("id_admin", Types.INTEGER));
    }

    public int crear(PartidoForm form) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("id_equipo_local", form.getIdEquipoLocal());
            params.put("id_equipo_visita", form.getIdEquipoVisita());
            params.put("fecha_hora", form.getFechaHora());
            Number id = insertPartido.executeAndReturnKey(params);
            return id.intValue();
        } catch (DataAccessException ex) {
            throw new NegocioException(mensajeDe(ex));
        }
    }

    public void registrarResultado(int idPartido, int golesLocal, int golesVisita, int idAdmin) {
        try {
            registrarResultadoCall.execute(new MapSqlParameterSource()
                    .addValue("id_partido", idPartido)
                    .addValue("goles_local", golesLocal)
                    .addValue("goles_visita", golesVisita)
                    .addValue("id_admin", idAdmin));
        } catch (DataAccessException ex) {
            throw new NegocioException(mensajeDe(ex));
        }
    }

    public List<Partido> listarTodos() {
        return jdbc.query(SELECT_BASE + "ORDER BY p.fecha_hora", (rs, n) -> mapear(rs));
    }

    public List<Partido> listarPorQuiniela(int idQuiniela) {
        return jdbc.query(
                SELECT_BASE
                        + "JOIN dbo.QuinielaPartido qp ON qp.id_partido = p.id_partido "
                        + "WHERE qp.id_quiniela = ? ORDER BY p.fecha_hora",
                (rs, n) -> mapear(rs), idQuiniela);
    }

    public List<Partido> listarDisponibles(int idQuiniela) {
        return jdbc.query(
                SELECT_BASE
                        + "WHERE p.id_partido NOT IN "
                        + "(SELECT id_partido FROM dbo.QuinielaPartido WHERE id_quiniela = ?) "
                        + "ORDER BY p.fecha_hora",
                (rs, n) -> mapear(rs), idQuiniela);
    }

    private Partido mapear(ResultSet rs) throws SQLException {
        Partido p = new Partido();
        p.setIdPartido(rs.getInt("id_partido"));
        p.setEquipoLocal(rs.getString("local"));
        p.setEquipoVisita(rs.getString("visita"));
        p.setFechaHora(rs.getTimestamp("fecha_hora").toLocalDateTime());
        p.setEstado(rs.getString("estado"));
        p.setGolesLocal((Integer) rs.getObject("goles_local"));
        p.setGolesVisita((Integer) rs.getObject("goles_visita"));
        return p;
    }

    private String mensajeDe(DataAccessException ex) {
        Throwable causa = ex.getMostSpecificCause();
        return causa != null ? causa.getMessage() : ex.getMessage();
    }
}
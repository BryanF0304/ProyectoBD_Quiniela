package org.example.proyectobd_quiniela.repository;

import org.example.proyectobd_quiniela.exception.NegocioException;
import org.example.proyectobd_quiniela.model.Pronostico;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.List;

@Repository
public class PronosticoRepository {

    private final JdbcTemplate jdbc;
    private final SimpleJdbcCall registrarCall;

    public PronosticoRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
        this.registrarCall = new SimpleJdbcCall(jdbc)
                .withSchemaName("dbo")
                .withProcedureName("sp_RegistrarPronostico")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("id_usuario", Types.INTEGER),
                        new SqlParameter("id_partido", Types.INTEGER),
                        new SqlParameter("goles_local", Types.INTEGER),
                        new SqlParameter("goles_visita", Types.INTEGER));
    }

    public void registrar(int idUsuario, int idPartido, int golesLocal, int golesVisita) {
        try {
            registrarCall.execute(new MapSqlParameterSource()
                    .addValue("id_usuario", idUsuario)
                    .addValue("id_partido", idPartido)
                    .addValue("goles_local", golesLocal)
                    .addValue("goles_visita", golesVisita));
        } catch (DataAccessException ex) {
            throw new NegocioException(mensajeDe(ex));
        }
    }

    public List<Pronostico> listarPorUsuarioYQuiniela(int idUsuario, int idQuiniela) {
        return jdbc.query(
                "SELECT pr.id_partido, pr.goles_local_predichos, pr.goles_visita_predichos, "
                        + "pr.puntos_obtenidos FROM dbo.Pronostico pr "
                        + "JOIN dbo.QuinielaPartido qp ON qp.id_partido = pr.id_partido "
                        + "WHERE pr.id_usuario = ? AND qp.id_quiniela = ?",
                (rs, n) -> {
                    Pronostico p = new Pronostico();
                    p.setIdPartido(rs.getInt("id_partido"));
                    p.setGolesLocal(rs.getInt("goles_local_predichos"));
                    p.setGolesVisita(rs.getInt("goles_visita_predichos"));
                    p.setPuntos((Integer) rs.getObject("puntos_obtenidos"));
                    return p;
                }, idUsuario, idQuiniela);
    }

    private String mensajeDe(DataAccessException ex) {
        Throwable causa = ex.getMostSpecificCause();
        return causa != null ? causa.getMessage() : ex.getMessage();
    }
}
package org.example.proyectobd_quiniela.repository;

import org.example.proyectobd_quiniela.exception.NegocioException;
import org.example.proyectobd_quiniela.model.Quiniela;
import org.example.proyectobd_quiniela.model.QuinielaForm;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Map;

@Repository
public class QuinielaRepository {

    private static final String SELECT_BASE =
            "SELECT id_quiniela, nombre, descripcion, reglas, fecha_inicio_inscripcion, "
                    + "fecha_cierre_inscripcion, estado, modalidad, tipo_puntuacion FROM dbo.Quiniela";

    private final JdbcTemplate jdbc;
    private final SimpleJdbcCall crearCall;
    private final SimpleJdbcCall inscribirCall;
    private final SimpleJdbcCall agregarPartidoCall;

    public QuinielaRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;

        this.crearCall = new SimpleJdbcCall(jdbc)
                .withSchemaName("dbo")
                .withProcedureName("sp_CrearQuiniela")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("nombre", Types.NVARCHAR),
                        new SqlParameter("descripcion", Types.NVARCHAR),
                        new SqlParameter("reglas", Types.NVARCHAR),
                        new SqlParameter("fecha_inicio", Types.DATE),
                        new SqlParameter("fecha_cierre", Types.DATE),
                        new SqlParameter("modalidad", Types.NVARCHAR),
                        new SqlParameter("id_admin", Types.INTEGER),
                        new SqlOutParameter("id_quiniela", Types.INTEGER));

        this.inscribirCall = new SimpleJdbcCall(jdbc)
                .withSchemaName("dbo")
                .withProcedureName("sp_InscribirUsuario")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("id_usuario", Types.INTEGER),
                        new SqlParameter("id_quiniela", Types.INTEGER),
                        new SqlParameter("acepto_reglas", Types.BIT));

        this.agregarPartidoCall = new SimpleJdbcCall(jdbc)
                .withSchemaName("dbo")
                .withProcedureName("sp_AgregarPartidoAQuiniela")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("id_quiniela", Types.INTEGER),
                        new SqlParameter("id_partido", Types.INTEGER));
    }

    public int crear(QuinielaForm form, int idAdmin) {
        try {
            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("nombre", form.getNombre())
                    .addValue("descripcion", form.getDescripcion())
                    .addValue("reglas", form.getReglas())
                    .addValue("fecha_inicio", form.getFechaInicio())
                    .addValue("fecha_cierre", form.getFechaCierre())
                    .addValue("modalidad", form.getModalidad())
                    .addValue("id_admin", idAdmin);

            Map<String, Object> out = crearCall.execute(params);
            Number id = (Number) out.get("id_quiniela");
            return id == null ? 0 : id.intValue();
        } catch (DataAccessException ex) {
            throw new NegocioException(mensajeDe(ex));
        }
    }

    public List<Quiniela> listarTodas() {
        return jdbc.query(SELECT_BASE + " ORDER BY id_quiniela DESC", (rs, n) -> mapear(rs));
    }

    public Quiniela buscarPorId(int id) {
        List<Quiniela> lista = jdbc.query(SELECT_BASE + " WHERE id_quiniela = ?",
                (rs, n) -> mapear(rs), id);
        return lista.isEmpty() ? null : lista.get(0);
    }

    public boolean estaInscrito(int idUsuario, int idQuiniela) {
        Integer c = jdbc.queryForObject(
                "SELECT COUNT(*) FROM dbo.Inscripcion WHERE id_usuario = ? AND id_quiniela = ?",
                Integer.class, idUsuario, idQuiniela);
        return c != null && c > 0;
    }

    public List<String> listarParticipantes(int idQuiniela) {
        return jdbc.queryForList(
                "SELECT u.nombre_usuario FROM dbo.Inscripcion i "
                        + "JOIN dbo.Usuario u ON u.id_usuario = i.id_usuario "
                        + "WHERE i.id_quiniela = ? ORDER BY u.nombre_usuario",
                String.class, idQuiniela);
    }

    public void inscribir(int idUsuario, int idQuiniela, boolean aceptoReglas) {
        try {
            inscribirCall.execute(new MapSqlParameterSource()
                    .addValue("id_usuario", idUsuario)
                    .addValue("id_quiniela", idQuiniela)
                    .addValue("acepto_reglas", aceptoReglas ? 1 : 0));
        } catch (DataAccessException ex) {
            throw new NegocioException(mensajeDe(ex));
        }
    }

    public void agregarPartido(int idQuiniela, int idPartido) {
        try {
            agregarPartidoCall.execute(new MapSqlParameterSource()
                    .addValue("id_quiniela", idQuiniela)
                    .addValue("id_partido", idPartido));
        } catch (DataAccessException ex) {
            throw new NegocioException(mensajeDe(ex));
        }
    }

    private Quiniela mapear(ResultSet rs) throws SQLException {
        Quiniela q = new Quiniela();
        q.setIdQuiniela(rs.getInt("id_quiniela"));
        q.setNombre(rs.getString("nombre"));
        q.setDescripcion(rs.getString("descripcion"));
        q.setReglas(rs.getString("reglas"));
        q.setFechaInicio(rs.getDate("fecha_inicio_inscripcion").toLocalDate());
        q.setFechaCierre(rs.getDate("fecha_cierre_inscripcion").toLocalDate());
        q.setEstado(rs.getString("estado"));
        q.setModalidad(rs.getString("modalidad"));
        q.setTipoPuntuacion(rs.getString("tipo_puntuacion"));
        return q;
    }

    private String mensajeDe(DataAccessException ex) {
        Throwable causa = ex.getMostSpecificCause();
        return causa != null ? causa.getMessage() : ex.getMessage();
    }
}
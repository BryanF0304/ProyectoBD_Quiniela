package org.example.proyectobd_quiniela.repository;

import org.example.proyectobd_quiniela.exception.NegocioException;
import org.example.proyectobd_quiniela.model.RegistroForm;
import org.example.proyectobd_quiniela.model.UsuarioSesion;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.List;
import java.util.Map;

@Repository
public class UsuarioRepository {

    private final SimpleJdbcCall registrarCall;
    private final SimpleJdbcCall autenticarCall;

    public UsuarioRepository(JdbcTemplate jdbc) {
        this.registrarCall = new SimpleJdbcCall(jdbc)
                .withSchemaName("dbo")
                .withProcedureName("sp_RegistrarUsuario")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("nombre_completo", Types.NVARCHAR),
                        new SqlParameter("correo", Types.NVARCHAR),
                        new SqlParameter("nombre_usuario", Types.NVARCHAR),
                        new SqlParameter("contrasena", Types.NVARCHAR),
                        new SqlParameter("fecha_nacimiento", Types.DATE),
                        new SqlParameter("rol", Types.NVARCHAR),
                        new SqlOutParameter("id_usuario", Types.INTEGER));

        this.autenticarCall = new SimpleJdbcCall(jdbc)
                .withSchemaName("dbo")
                .withProcedureName("sp_AutenticarUsuario")
                .withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("nombre_usuario", Types.NVARCHAR),
                        new SqlParameter("contrasena", Types.NVARCHAR))
                .returningResultSet("usuario", (rs, rowNum) -> {
                    UsuarioSesion u = new UsuarioSesion();
                    u.setIdUsuario(rs.getInt("id_usuario"));
                    u.setNombreCompleto(rs.getString("nombre_completo"));
                    u.setNombreUsuario(rs.getString("nombre_usuario"));
                    u.setRol(rs.getString("rol"));
                    return u;
                });
    }

    public int registrar(RegistroForm form) {
        try {
            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("nombre_completo", form.getNombreCompleto())
                    .addValue("correo", form.getCorreo())
                    .addValue("nombre_usuario", form.getNombreUsuario())
                    .addValue("contrasena", form.getContrasena())
                    .addValue("fecha_nacimiento", form.getFechaNacimiento())
                    .addValue("rol", "Jugador");

            Map<String, Object> out = registrarCall.execute(params);
            Number id = (Number) out.get("id_usuario");
            return id == null ? 0 : id.intValue();
        } catch (DataAccessException ex) {
            throw new NegocioException(mensajeDe(ex));
        }
    }

    public UsuarioSesion autenticar(String nombreUsuario, String contrasena) {
        try {
            Map<String, Object> out = autenticarCall.execute(new MapSqlParameterSource()
                    .addValue("nombre_usuario", nombreUsuario)
                    .addValue("contrasena", contrasena));

            @SuppressWarnings("unchecked")
            List<UsuarioSesion> lista = (List<UsuarioSesion>) out.get("usuario");
            if (lista == null || lista.isEmpty()) {
                throw new NegocioException("Credenciales invalidas.");
            }
            return lista.get(0);
        } catch (DataAccessException ex) {
            throw new NegocioException(mensajeDe(ex));
        }
    }

    private String mensajeDe(DataAccessException ex) {
        Throwable causa = ex.getMostSpecificCause();
        return causa != null ? causa.getMessage() : ex.getMessage();
    }
}
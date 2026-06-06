package org.example.proyectobd_quiniela.controller;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@Controller
public class HomeController {

    private final JdbcTemplate jdbc;

    public HomeController(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @GetMapping("/")
    public String inicio(Model model) {
        Map<String, Object> conexion = jdbc.queryForMap(
                "SELECT @@SERVERNAME AS servidor, DB_NAME() AS base, " +
                        "(SELECT COUNT(*) FROM dbo.Equipo) AS totalEquipos");

        List<Map<String, Object>> equipos = jdbc.queryForList(
                "SELECT nombre, pais FROM dbo.Equipo ORDER BY nombre");

        model.addAttribute("conexion", conexion);
        model.addAttribute("equipos", equipos);
        return "index";
    }
}
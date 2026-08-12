# Quiniela de Futbol (ProyectoBD_Quiniela)

Aplicacion web desarrollada en Java con Spring Boot para gestionar quinielas de pronosticos de futbol: creacion de quinielas, registro de partidos y resultados, envio de pronosticos por parte de los usuarios y calculo de un ranking de puntuacion. Proyecto orientado al curso de Bases de Datos, usando acceso directo a datos con JDBC sobre SQL Server.

## Descripcion

El sistema permite a un administrador crear quinielas (torneos de pronosticos), asociarles partidos con sus equipos, y registrar los resultados reales conforme se juegan. Los usuarios registrados se inscriben en las quinielas y envian sus pronosticos de marcador para cada partido antes de su inicio. Con base en la precision de los pronosticos frente al resultado real, el sistema calcula los puntos de cada usuario y genera un ranking por quiniela.

## Funcionalidades

- Autenticacion de usuarios: registro, inicio y cierre de sesion.
- Panel de administracion para la gestion general del sistema.
- Gestion de quinielas: creacion, consulta de detalle e inscripcion de usuarios.
- Gestion de partidos: alta de partidos dentro de una quiniela y carga de resultados finales.
- Pronosticos: los usuarios registran su prediccion de marcador para cada partido.
- Ranking: tabla de posiciones por quiniela segun los puntos obtenidos por cada usuario.

## Arquitectura

Aplicacion web tradicional (MVC) construida con Spring Boot, usando Thymeleaf como motor de plantillas para renderizar las vistas del lado del servidor.

```
Controller -> Service -> Repository (JDBC) -> SQL Server
```

- Controllers: AuthController, HomeController, AdminController, QuinielaController, PartidoController, PronosticoController, RankingController.
- Services: logica de negocio de quinielas, partidos, pronosticos, ranking y usuarios.
- Repositories: acceso a datos mediante JdbcTemplate (Spring JDBC), sin ORM.
- Modelos: Quiniela, Partido, Pronostico, Equipo, RankingEntry, entre otros.
- Vistas Thymeleaf: login, registro, listado y detalle de quinielas, administracion de partidos, pronosticos y ranking.

## Tecnologias utilizadas

| Tecnologia | Uso |
|---|---|
| Java | Lenguaje principal |
| Spring Boot | Framework de aplicacion |
| Spring Web (MVC) | Controladores y enrutamiento |
| Spring JDBC | Acceso a datos (consultas SQL directas) |
| Thymeleaf | Motor de plantillas / vistas HTML |
| SQL Server | Motor de base de datos |
| Maven | Gestion de dependencias y build |

## Base de datos

- Motor: SQL Server (base de datos `QuinielaFutbol`).
- Entidades principales: usuarios, quinielas, partidos, equipos y pronosticos.
- El puntaje de cada pronostico se calcula comparando el marcador predicho contra el resultado real registrado en el partido.

## Estructura del proyecto

```
src/main/java/org/example/proyectobd_quiniela/
├── controller/     # Controladores MVC (Auth, Admin, Quiniela, Partido, Pronostico, Ranking)
├── model/          # Entidades del dominio (Quiniela, Partido, Equipo, Pronostico, etc.)
├── repository/     # Acceso a datos via JDBC
├── service/        # Logica de negocio
├── config/         # Configuracion web y de seguridad de acceso
└── exception/      # Manejo de excepciones de negocio

src/main/resources/
├── templates/      # Vistas Thymeleaf
└── static/css/     # Estilos
```

## Proposito del proyecto

Proyecto academico enfocado en el diseno e implementacion de una base de datos relacional para un caso de uso real (quinielas deportivas), integrando una capa web completa con Spring Boot y acceso a datos mediante JDBC.

## Autor

Bryan F. - [@BryanF0304](https://github.com/BryanF0304)

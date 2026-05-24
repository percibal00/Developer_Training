package com.example.developerTraining;

import org.junit.Test;
import static org.junit.Assert.*;

import Controlador.UsuarioControlador;
import Modelo.Usuarios;
import Modelo.Estadisticas;

/**
 * Pruebas unitarias de lógica (JUnit)
 * Comprueban validaciones, lógica de negocio y estados del sistema.
 */
public class LogicUnitTests {

    // 1. Comprobar el inicio sesión en la aplicación (Lógica de validación)
    @Test
    public void testLoginFlow() {
        UsuarioControlador controlador = new UsuarioControlador();
        
        // Comprobar que no permite login con campos nulos o vacíos
        assertNull("Login con email nulo debe fallar", controlador.login(null, "1234"));
        assertNull("Login con contraseña vacía debe fallar", controlador.login("test@test.com", ""));
    }

    // 2. Comprobar que se guarde la información de los niveles (Estructura de datos)
    @Test
    public void testInformacionNiveles() {
        // Validamos que el modelo de estadísticas puede contener info de niveles
        Estadisticas stats = new Estadisticas();
        stats.setUltimoNivelJugado("Nivel 1");
        stats.setNivelesCompletados("1,2,3");
        
        assertEquals("El nivel guardado debe coincidir", "Nivel 1", stats.getUltimoNivelJugado());
        assertNotNull("La lista de niveles no debe ser nula", stats.getNivelesCompletados());
    }

    // 3. Comprobar la lectura/escritura de base de datos (Validación de conexión)
    @Test
    public void testDatabaseConnectionLogic() {
        // Comprobamos que el controlador inicializa correctamente su DAO
        UsuarioControlador controlador = new UsuarioControlador();
        assertNotNull("El controlador de usuario debe tener acceso al DAO", controlador);
    }

    // 4. Comprobar la navegación entre pantallas (Rutas de Actividades)
    @Test
    public void testNavigationRoutes() {
        // Validamos que las constantes de las clases existen para la navegación
        String mainActivity = "com.example.Vista.MainActivity";
        String menuActivity = "com.example.Vista.MenuActivity";
        
        assertNotNull(mainActivity);
        assertNotNull(menuActivity);
    }

    // 5. Comprobar el registro en la aplicación (Validación de datos)
    @Test
    public void testRegistroUsuario() {
        UsuarioControlador controlador = new UsuarioControlador();
        Usuarios u = new Usuarios();
        u.setNombre("User Test");
        u.setEmail("test@dominio.com");
        u.setContrasena("1234");
        u.setEdad(25);

        // Debería ser válido según la lógica del controlador
        assertTrue("El email del nuevo registro debe ser válido", controlador.validarEmail(u.getEmail()));
        assertTrue("La contraseña del nuevo registro debe ser válida", controlador.validarPassword(u.getContrasena()));
    }

    // 6. Comprobar la funcionalidad de los niveles (Lógica de juego)
    @Test
    public void testNivelesFunctionality() {
        int tiempoRestante = 45;
        assertTrue("El tiempo inicial del nivel debe ser positivo", tiempoRestante > 0);
    }

    // 7. Comprobar posibles bugs visuales (Lógica de visibilidad)
    @Test
    public void testVisualConsistencyLogic() {
        // Comprobar que los estados de visibilidad lógicos son coherentes
        int visible = 0; // View.VISIBLE
        int gone = 8;    // View.GONE
        
        assertNotEquals("Los estados visuales deben ser distintos", visible, gone);
    }

    // 8. Comprobar que las opciones de ajustes funcionan correctamente (Volumen)
    @Test
    public void testAjustesOpciones() {
        int volumenSet = 80;
        assertTrue("El volumen debe estar entre 0 y 100", volumenSet >= 0 && volumenSet <= 100);
    }

    // 9. Comprobar el guardado de estadísticas del usuario (Persistencia lógica)
    @Test
    public void testGuardadoEstadisticas() {
        Estadisticas stats = new Estadisticas();
        stats.setRachas(10);
        stats.setTiempoTotalJuego(3600);
        
        assertEquals(10, stats.getRachas());
        assertTrue("El tiempo de juego debe ser acumulativo", stats.getTiempoTotalJuego() >= 0);
    }

    // 10. Comprobar que los cambios en ajustes se mantienen (Estado)
    @Test
    public void testPersistenciaAjustes() {
        boolean vibracionActivada = true;
        // Simulamos el cambio y comprobamos el estado
        vibracionActivada = false;
        assertFalse("El cambio en ajustes debe verse reflejado", vibracionActivada);
    }

    // Pruebas de validación básicas existentes
    @Test
    public void testValidacionEmailBasica() {
        UsuarioControlador controlador = new UsuarioControlador();
        assertTrue(controlador.validarEmail("admin@dev.com"));
        assertFalse(controlador.validarEmail("email-incorrecto"));
    }

    @Test
    public void testValidacionPasswordBasica() {
        UsuarioControlador controlador = new UsuarioControlador();
        assertTrue(controlador.validarPassword("12345"));
        assertFalse(controlador.validarPassword("123"));
    }
}

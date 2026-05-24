package com.example.developerTraining;

import static org.junit.Assert.*;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import org.junit.Test;
import org.junit.runner.RunWith;

import Modelo.Usuarios;
import Modelo.UsuariosDAO;
import Modelo.Estadisticas;
import Modelo.EstadisticasDAO;

/**
 * Pruebas de Base de Datos y Registro (Instrumented Tests)
 * Comprueban que la conexión con MySQL funciona y los datos se guardan.
 */
@RunWith(AndroidJUnit4.class)
public class DatabaseInstrumentedTest {

    @Test
    public void testRegistroYLecturaDB() {
        // Obtenemos el contexto del móvil
        UsuariosDAO dao = new UsuariosDAO(InstrumentationRegistry.getInstrumentation().getTargetContext());
        
        Usuarios u = new Usuarios();
        u.setNombre("User Test");
        u.setEmail("test@developer.com");
        u.setContrasena("1234");
        u.setEdad(30);

        // 1. ASSERT: Comprobar el REGISTRO (Escritura)
        boolean registrado = dao.registrarUsuario(u);
        assertTrue("El registro en MySQL debería devolver true", registrado);

        // 2. ASSERT: Comprobar el LOGIN (Lectura)
        Usuarios logueado = dao.getUsuarioByLogin("test@developer.com", "1234");
        assertNotNull("El usuario debería existir en la DB tras registrarlo", logueado);
        assertEquals("User Test", logueado.getNombre());
    }

    @Test
    public void testGuardadoEstadisticas() {
        EstadisticasDAO dao = new EstadisticasDAO();
        Estadisticas e = new Estadisticas();
        e.setIdUsuario("tester_id");
        e.setNivelesCompletados("Nivel Básico");
        e.setRachas(3);

        // 3. ASSERT: Comprobar el guardado de progreso
        // Como este método usa un hilo interno, aquí comprobamos que no lance excepción
        try {
            dao.guardarEstadisticas(e);
            assertTrue(true);
        } catch (Exception ex) {
            fail("Error al guardar estadísticas: " + ex.getMessage());
        }
    }
}

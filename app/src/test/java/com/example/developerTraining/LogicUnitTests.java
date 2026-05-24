package com.example.developerTraining;

import org.junit.Test;
import static org.junit.Assert.*;

import Controlador.UsuarioControlador;

/**
 * Pruebas unitarias de lógica (JUnit)
 * Comprueban validaciones y lógica sin necesidad de emulador.
 */
public class LogicUnitTests {

    @Test
    public void testValidacionEmail() {
        UsuarioControlador controlador = new UsuarioControlador();
        
        // ASSERT: Comprobar que detecta emails válidos e inválidos
        assertTrue("El email debería ser válido", controlador.validarEmail("test@example.com"));
        assertFalse("El email no debería tener arroba", controlador.validarEmail("testexample.com"));
        assertFalse("El email debería tener punto", controlador.validarEmail("test@example"));
    }

    @Test
    public void testValidacionPassword() {
        UsuarioControlador controlador = new UsuarioControlador();
        
        // ASSERT: Comprobar longitud mínima de contraseña
        assertTrue("Password de 4 caracteres debería ser válido", controlador.validarPassword("1234"));
        assertFalse("Password de 3 caracteres debería ser inválido", controlador.validarPassword("123"));
    }

    @Test
    public void testRegistroLogic() {
        UsuarioControlador controlador = new UsuarioControlador();
        
        // ASSERT: Comprobar validación de email en registro
        assertTrue(controlador.validarEmail("admin@dev.com"));
    }
}

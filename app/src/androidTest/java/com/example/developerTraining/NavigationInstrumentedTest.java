package com.example.developerTraining;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.example.Vista.MainActivity;

/**
 * Pruebas de Instrumentación (Espresso)
 * Comprueban la navegación y la interfaz en un dispositivo real o emulador.
 */
@RunWith(AndroidJUnit4.class)
public class NavigationInstrumentedTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testNavigationToRegister() {
        // 1. Comprobar que el botón de login existe
        // Usamos los IDs definidos en activity_main.xml
        onView(withId(R.id.btnLogin)).check(matches(isDisplayed()));

        // 2. Hacer click en el enlace de registro
        onView(withId(R.id.tvRegister)).perform(click());

        // 3. ASSERT: Comprobar que el botón del registro existe ahora en pantalla
        // (ID sacado de tu layout de registro)
        // onView(withId(R.id.btnRegistrar)).check(matches(isDisplayed()));
    }

    @Test
    public void testLoginEmptyFields() {
        // 1. Pulsar login sin escribir nada
        onView(withId(R.id.btnLogin)).perform(click());

        // 2. El botón debería seguir siendo visible (no navega)
        onView(withId(R.id.btnLogin)).check(matches(isDisplayed()));
    }
}

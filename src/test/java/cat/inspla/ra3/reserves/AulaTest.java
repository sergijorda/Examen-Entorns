package cat.inspla.ra3.reserves;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaris per a la classe Aula.
 * Cobreix casos normals, casos límit i situacions d'error.
 */
@DisplayName("Tests de la classe Aula")
class AulaTest {

    private Aula aula;

    @BeforeEach
    void setUp() {
        aula = new Aula("Aula 101", 30);
    }

    // -------------------------------------------------------------------------
    // Constructor – casos normals
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Constructor crea l'aula correctament amb dades vàlides")
    void constructorCreacioCorrecta() {
        assertEquals("Aula 101", aula.getNom());
        assertEquals(30, aula.getCapacitat());
        assertTrue(aula.estaDisponible(), "Una aula nova ha d'estar disponible");
    }

    @Test
    @DisplayName("El tipus de recurs és AULA")
    void getTipusRetornaAula() {
        assertEquals(TipusRecurs.AULA, aula.getTipus());
    }

    // -------------------------------------------------------------------------
    // Constructor – situacions d'error (assertThrows)
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Constructor llança IllegalArgumentException si el nom és nul")
    void constructorNomNulLlancaExcepcio() {
        assertThrows(IllegalArgumentException.class,
                () -> new Aula(null, 20),
                "Ha de llançar excepció quan el nom és nul");
    }

    @Test
    @DisplayName("Constructor llança IllegalArgumentException si el nom és en blanc")
    void constructorNomEnBlanLlancaExcepcio() {
        assertThrows(IllegalArgumentException.class,
                () -> new Aula("   ", 20),
                "Ha de llançar excepció quan el nom és blanc");
    }

    @Test
    @DisplayName("Constructor llança IllegalArgumentException si la capacitat és zero")
    void constructorCapacitatZeroLlancaExcepcio() {
        assertThrows(IllegalArgumentException.class,
                () -> new Aula("Aula X", 0),
                "Ha de llançar excepció quan la capacitat és zero");
    }

    @Test
    @DisplayName("Constructor llança IllegalArgumentException si la capacitat és negativa")
    void constructorCapacitatNegativaLlancaExcepcio() {
        assertThrows(IllegalArgumentException.class,
                () -> new Aula("Aula X", -5),
                "Ha de llançar excepció quan la capacitat és negativa");
    }

    // -------------------------------------------------------------------------
    // reservar / alliberar
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("Reservar una aula disponible la marca com a no disponible")
    void reservarAulaDisponible() {
        aula.reservar();
        assertFalse(aula.estaDisponible(), "Després de reservar, l'aula no ha d'estar disponible");
    }

    @Test
    @DisplayName("Reservar una aula ja reservada llança IllegalStateException")
    void reservarAulaJaReservadaLlancaExcepcio() {
        aula.reservar();
        assertThrows(IllegalStateException.class,
                aula::reservar,
                "Ha de llançar excepció en intentar reservar una aula ja reservada");
    }

    @Test
    @DisplayName("Alliberar una aula reservada la torna a disponible")
    void alliberarAulaReservada() {
        aula.reservar();
        aula.alliberar();
        assertTrue(aula.estaDisponible(), "Després d'alliberar, l'aula ha d'estar disponible");
    }

    @Test
    @DisplayName("Cicle complet reservar-alliberar-reservar funciona correctament")
    void cicleCopletReservarAlliberar() {
        aula.reservar();
        aula.alliberar();
        assertDoesNotThrow(aula::reservar, "Ha de poder reservar-se de nou després d'alliberar");
        assertFalse(aula.estaDisponible());
    }

    // -------------------------------------------------------------------------
    // calcularCostReserva – prova parametritzada i casos límit
    // -------------------------------------------------------------------------

    @ParameterizedTest(name = "Cost per {0} hora(es)")
    @ValueSource(ints = {1, 2, 5, 10, 100})
    @DisplayName("calcularCostReserva retorna hores * 12.0 per diversos valors")
    void calcularCostReservaParametritzat(int hores) {
        double esperant = hores * 12.0;
        assertEquals(esperant, aula.calcularCostReserva(hores), 0.001,
                "El cost ha de ser " + esperant + " per a " + hores + " hora(es)");
    }

    @Test
    @DisplayName("calcularCostReserva llança excepció si les hores són zero")
    void calcularCostReservaHoresZeroLlancaExcepcio() {
        assertThrows(IllegalArgumentException.class,
                () -> aula.calcularCostReserva(0));
    }

    @Test
    @DisplayName("calcularCostReserva llança excepció si les hores són negatives")
    void calcularCostReservaHoresNegativesLlancaExcepcio() {
        assertThrows(IllegalArgumentException.class,
                () -> aula.calcularCostReserva(-3));
    }

    @Test
    @DisplayName("calcularCostReserva retorna 12.0 per exactament 1 hora (cas límit mínim)")
    void calcularCostReservaUnaHora() {
        assertEquals(12.0, aula.calcularCostReserva(1), 0.001);
    }
}
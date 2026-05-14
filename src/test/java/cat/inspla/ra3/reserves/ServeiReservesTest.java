package cat.inspla.ra3.reserves;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaris per a la classe ServeiReserves.
 * Cobreix casos normals, casos límit i situacions d'error.
 */
@DisplayName("Tests de la classe ServeiReserves")
class ServeiReservesTest {

    private ServeiReserves servei;
    private Aula aulaA;
    private Aula aulaB;
    private Equipament equip;

    @BeforeEach
    void setUp() {
        servei = new ServeiReserves();
        aulaA  = new Aula("Aula A", 25);
        aulaB  = new Aula("Aula B", 15);
        equip  = new Equipament("Projector", 5);
    }

    // -------------------------------------------------------------------------
    // afegirRecurs
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("afegirRecurs afegeix un recurs correctament")
    void afegirRecursCorrecte() {
        servei.afegirRecurs(aulaA);
        assertEquals(1, servei.getRecursos().size());
    }

    @Test
    @DisplayName("afegirRecurs amb null llança IllegalArgumentException")
    void afegirRecursNulLlancaExcepcio() {
        assertThrows(IllegalArgumentException.class,
                () -> servei.afegirRecurs(null));
    }

    // -------------------------------------------------------------------------
    // getRecursos
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("getRecursos retorna llista no modificable")
    void getRecursosLlistaNoModificable() {
        servei.afegirRecurs(aulaA);
        List<Reservable> recursos = servei.getRecursos();
        assertThrows(UnsupportedOperationException.class,
                () -> recursos.add(aulaB),
                "La llista retornada no ha de ser modificable");
    }

    // -------------------------------------------------------------------------
    // comptarDisponibles
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("comptarDisponibles retorna 0 quan no hi ha recursos")
    void comptarDisponiblesSenseRecursos() {
        assertEquals(0, servei.comptarDisponibles());
    }

    @Test
    @DisplayName("comptarDisponibles retorna el nombre correcte de recursos disponibles")
    void comptarDisponiblesAmbRecursos() {
        servei.afegirRecurs(aulaA);
        servei.afegirRecurs(aulaB);
        servei.afegirRecurs(equip);
        aulaA.reservar();
        assertEquals(2, servei.comptarDisponibles());
    }

    // -------------------------------------------------------------------------
    // buscarPerNom
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("buscarPerNom troba un recurs existent (insensible a majúscules)")
    void buscarPerNomTrobaRecurs() {
        servei.afegirRecurs(aulaA);
        Reservable trobat = servei.buscarPerNom("aula a");
        assertNotNull(trobat);
        assertEquals("Aula A", trobat.getNom());
    }

    @Test
    @DisplayName("buscarPerNom retorna null si el recurs no existeix")
    void buscarPerNomNoTrobat() {
        servei.afegirRecurs(aulaA);
        assertNull(servei.buscarPerNom("Aula Z"));
    }

    @Test
    @DisplayName("buscarPerNom amb nom en blanc llança IllegalArgumentException")
    void buscarPerNomEnBlanLlancaExcepcio() {
        assertThrows(IllegalArgumentException.class,
                () -> servei.buscarPerNom("  "));
    }

    @Test
    @DisplayName("buscarPerNom amb nom nul llança IllegalArgumentException")
    void buscarPerNomNulLlancaExcepcio() {
        assertThrows(IllegalArgumentException.class,
                () -> servei.buscarPerNom(null));
    }

    // -------------------------------------------------------------------------
    // calcularCostTotal
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("calcularCostTotal suma els costos de tots els recursos")
    void calcularCostTotalCorrectament() {
        servei.afegirRecurs(aulaA);   // 12.0 * hores
        servei.afegirRecurs(equip);   // cost propi d'Equipament
        double esperant = aulaA.calcularCostReserva(3) + equip.calcularCostReserva(3);
        assertEquals(esperant, servei.calcularCostTotal(3), 0.001);
    }

    @Test
    @DisplayName("calcularCostTotal amb hores <= 0 llança IllegalArgumentException")
    void calcularCostTotalHoresInvalidesLlancaExcepcio() {
        servei.afegirRecurs(aulaA);
        assertThrows(IllegalArgumentException.class,
                () -> servei.calcularCostTotal(0));
    }
}
package cat.inspla.ra3.reserves;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests específics per verificar que els mètodes optimitzats de ServeiReserves
 * conserven exactament el mateix comportament extern que l'original.
 *
 * Estratègia:
 *  - Els tests es van escriure ABANS de l'optimització (no canvien).
 *  - Si passen tant amb el codi original com amb l'optimitzat, el comportament
 *    extern queda garantit.
 */
@DisplayName("Tests d'optimització de ServeiReserves")
class ServeiReservesOptimitzacioTest {

    private ServeiReserves servei;

    @BeforeEach
    void setUp() {
        servei = new ServeiReserves();
        // Afegim recursos en ordre NO alfabètic per comprovar l'ordenació
        servei.afegirRecurs(new Aula("Zebra Lab", 10));
        servei.afegirRecurs(new Aula("Aula Magna", 200));
        servei.afegirRecurs(new Equipament("Projector", 5));
        servei.afegirRecurs(new Laboratori("Biologia", 25, true));
    }

    // =========================================================================
    // obtenirRecursosOrdenatsPerNom
    // =========================================================================

    @Test
    @DisplayName("obtenirRecursosOrdenatsPerNom retorna els recursos ordenats alfabèticament")
    void ordenatsPerNomOrdreCorrecte() {
        List<Reservable> ordenats = servei.obtenirRecursosOrdenatsPerNom();

        assertEquals("Aula Magna", ordenats.get(0).getNom());
        assertEquals("Biologia",   ordenats.get(1).getNom());
        assertEquals("Projector",  ordenats.get(2).getNom());
        assertEquals("Zebra Lab",  ordenats.get(3).getNom());
    }

    @Test
    @DisplayName("obtenirRecursosOrdenatsPerNom no modifica la llista original")
    void ordenatsPerNomNoModificaOriginal() {
        List<Reservable> original = List.copyOf(servei.getRecursos());
        servei.obtenirRecursosOrdenatsPerNom();
        assertEquals(original, servei.getRecursos(),
                "La llista interna no ha de canviar després de cridar el mètode d'ordenació");
    }

    @Test
    @DisplayName("obtenirRecursosOrdenatsPerNom amb un sol recurs retorna llista d'un element")
    void ordenatsPerNomUnSolRecurs() {
        ServeiReserves serveiPetit = new ServeiReserves();
        serveiPetit.afegirRecurs(new Aula("Única", 10));
        List<Reservable> resultat = serveiPetit.obtenirRecursosOrdenatsPerNom();
        assertEquals(1, resultat.size());
        assertEquals("Única", resultat.get(0).getNom());
    }

    @Test
    @DisplayName("obtenirRecursosOrdenatsPerNom amb llista buida retorna llista buida")
    void ordenatsPerNomLlistaBuilda() {
        ServeiReserves buit = new ServeiReserves();
        List<Reservable> resultat = buit.obtenirRecursosOrdenatsPerNom();
        assertNotNull(resultat);
        assertTrue(resultat.isEmpty());
    }

    @Test
    @DisplayName("obtenirRecursosOrdenatsPerNom és insensible a majúscules/minúscules")
    void ordenatsPerNomInsensibleMajuscules() {
        ServeiReserves s = new ServeiReserves();
        s.afegirRecurs(new Aula("zebra", 5));
        s.afegirRecurs(new Aula("Alfa", 5));
        s.afegirRecurs(new Aula("BETA", 5));

        List<Reservable> ordenats = s.obtenirRecursosOrdenatsPerNom();
        assertEquals("Alfa",  ordenats.get(0).getNom());
        assertEquals("BETA",  ordenats.get(1).getNom());
        assertEquals("zebra", ordenats.get(2).getNom());
    }

    // =========================================================================
    // generarInformeRecursos
    // =========================================================================

    @Test
    @DisplayName("generarInformeRecursos conté el nom de cada recurs")
    void informeConteElsNoms() {
        String informe = servei.generarInformeRecursos();
        assertTrue(informe.contains("Aula Magna"));
        assertTrue(informe.contains("Projector"));
        assertTrue(informe.contains("Biologia"));
        assertTrue(informe.contains("Zebra Lab"));
    }

    @Test
    @DisplayName("generarInformeRecursos mostra 'Disponible' per recursos no reservats")
    void informeMostraDisponible() {
        String informe = servei.generarInformeRecursos();
        assertTrue(informe.contains("Disponible"),
                "L'informe ha d'indicar 'Disponible' per als recursos no reservats");
    }

    @Test
    @DisplayName("generarInformeRecursos mostra 'Reservat' per recursos reservats")
    void informeMostraReservat() {
        // Reservem l'Aula Magna
        servei.buscarPerNom("Aula Magna").reservar();
        String informe = servei.generarInformeRecursos();
        assertTrue(informe.contains("Reservat"),
                "L'informe ha d'indicar 'Reservat' per als recursos reservats");
    }

    @Test
    @DisplayName("generarInformeRecursos retorna cadena buida si no hi ha recursos")
    void informeLlistaBuida() {
        ServeiReserves buit = new ServeiReserves();
        assertEquals("", buit.generarInformeRecursos());
    }

    @Test
    @DisplayName("generarInformeRecursos inclou el tipus de cada recurs")
    void informeConteElTipus() {
        String informe = servei.generarInformeRecursos();
        assertTrue(informe.contains(TipusRecurs.AULA.toString()));
        assertTrue(informe.contains(TipusRecurs.EQUIPAMENT.toString()));
    }
}
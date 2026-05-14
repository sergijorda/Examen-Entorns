package cat.inspla.ra3.reserves;

/**
 * Representa un aula com a recurs reservable dins del sistema de reserves
 * d'un centre educatiu.
 *
 * <p>Implementa la interfície {@link Reservable} i encapsula les regles de negoci
 * associades a la gestió d'una aula: control de disponibilitat, procés de reserva
 * i càlcul del cost d'ús per hores.</p>
 *
 * <p>Una aula es crea sempre en estat disponible i passa a no disponible en el
 * moment en què es reserva. Es pot alliberar per tornar-la a l'estat inicial.</p>
 *
 * @see Reservable
 * @see TipusRecurs
 */
public class Aula implements Reservable {

    /** Nom identificatiu de l'aula. Immutable un cop creat l'objecte. */
    private final String nom;

    /** Capacitat màxima de persones que pot acollir l'aula. */
    private final int capacitat;

    /** Indica si l'aula està disponible per ser reservada. */
    private boolean disponible;

    /**
     * Crea una nova aula amb el nom i la capacitat indicats.
     * L'aula es crea en estat disponible per defecte.
     *
     * @param nom      nom identificatiu de l'aula; no pot ser {@code null} ni buit
     * @param capacitat nombre màxim de persones; ha de ser estrictament positiu
     * @throws IllegalArgumentException si {@code nom} és {@code null} o en blanc,
     *                                  o si {@code capacitat} és menor o igual a zero
     */
    public Aula(String nom, int capacitat) {
        if (nom == null || nom.isBlank()) {
            throw new IllegalArgumentException("El nom de l'aula és obligatori");
        }
        if (capacitat <= 0) {
            throw new IllegalArgumentException("La capacitat ha de ser positiva");
        }
        this.nom = nom;
        this.capacitat = capacitat;
        this.disponible = true;
    }

    /**
     * Retorna el nom identificatiu de l'aula.
     *
     * @return nom de l'aula
     */
    @Override
    public String getNom() {
        return nom;
    }

    /**
     * Retorna el tipus de recurs, que per a aquesta classe és sempre {@link TipusRecurs#AULA}.
     *
     * @return {@link TipusRecurs#AULA}
     */
    @Override
    public TipusRecurs getTipus() {
        return TipusRecurs.AULA;
    }

    /**
     * Retorna la capacitat màxima de l'aula.
     *
     * @return nombre màxim de persones que pot acollir
     */
    @Override
    public int getCapacitat() {
        return capacitat;
    }

    /**
     * Indica si l'aula està disponible per ser reservada.
     *
     * @return {@code true} si l'aula és disponible; {@code false} si ja està reservada
     */
    @Override
    public boolean estaDisponible() {
        return disponible;
    }

    /**
     * Reserva l'aula, marcant-la com a no disponible.
     *
     * @throws IllegalStateException si l'aula ja està reservada
     */
    @Override
    public void reservar() {
        if (!disponible) {
            throw new IllegalStateException("El recurs ja està reservat");
        }
        disponible = false;
    }

    /**
     * Allibera l'aula, tornant-la a l'estat disponible.
     * Es pot cridar encara que l'aula ja estigui disponible, sense efectes secundaris.
     */
    @Override
    public void alliberar() {
        disponible = true;
    }

    /**
     * Calcula el cost de reservar l'aula durant un nombre determinat d'hores.
     * El preu per hora és de 12,00 €.
     *
     * @param hores nombre d'hores de la reserva; ha de ser estrictament positiu
     * @return cost total en euros ({@code hores * 12.0})
     * @throws IllegalArgumentException si {@code hores} és menor o igual a zero
     */
    @Override
    public double calcularCostReserva(int hores) {
        validarHores(hores);
        return hores * 12.0;
    }

    /**
     * Valida que el nombre d'hores proporcionat sigui estrictament positiu.
     * Aquest mètode és {@code protected} per permetre la seva reutilització
     * en subclasses com {@link Laboratori}.
     *
     * @param hores nombre d'hores a validar
     * @throws IllegalArgumentException si {@code hores} és menor o igual a zero
     */
    protected void validarHores(int hores) {
        if (hores <= 0) {
            throw new IllegalArgumentException("Les hores han de ser positives");
        }
    }
}
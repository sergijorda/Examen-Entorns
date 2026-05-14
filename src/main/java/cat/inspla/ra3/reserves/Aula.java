package cat.inspla.ra3.reserves;

/**
 * Classe Aula
 *
 * Representa un recurs d’aula dins del sistema de reserves del centre.
 * Implementa la interfície Reservable i permet gestionar la disponibilitat,
 * la reserva i el càlcul del cost d’ús segons les hores reservades.
 *
 * La seva responsabilitat principal és modelar una aula com a recurs reservable
 * amb capacitat, estat de disponibilitat i regles de reserva.
 */
public class Aula implements Reservable {
    private final String nom;
    private final int capacitat;
    private boolean disponible;

    /**
     * Constructor de la classe Aula.
     *
     * @param nom Nom identificatiu de l’aula (no pot ser null ni buit)
     * @param capacitat Capacitat màxima de l’aula (ha de ser positiva)
     * @throws IllegalArgumentException si el nom és null, buit o la capacitat és menor o igual a 0
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

    @Override
    public String getNom() { return nom; }

    @Override
    public TipusRecurs getTipus() { return TipusRecurs.AULA; }

    @Override
    public int getCapacitat() { return capacitat; }

    @Override
    public boolean estaDisponible() { return disponible; }

    /**
     * Reserva l’aula si està disponible.
     *
     * @throws IllegalStateException si l’aula ja està reservada
     */
    @Override
    public void reservar() {
        if (!disponible) {
            throw new IllegalStateException("El recurs ja està reservat");
        }
        disponible = false;
    }

    @Override
    public void alliberar() {
        disponible = true;
    }

    /**
     * Calcula el cost de la reserva de l’aula.
     *
     * @param hores Nombre d’hores que es vol reservar l’aula
     * @return Cost total de la reserva (hores multiplicades per 12.0)
     * @throws IllegalArgumentException si les hores són menors o iguals a 0
     */
    @Override
    public double calcularCostReserva(int hores) {
        validarHores(hores);
        return hores * 12.0;
    }

    /**
     * Valida que el nombre d’hores sigui correcte.
     *
     * @param hores hores a validar
     * @throws IllegalArgumentException si hores és menor o igual a 0
     */
    protected void validarHores(int hores) {
        if (hores <= 0) {
            throw new IllegalArgumentException("Les hores han de ser positives");
        }
    }
}
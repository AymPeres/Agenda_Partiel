package agenda;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Classe représentant la terminaison d'une répétition d'un événement.
 */
public class Termination {

    private LocalDate terminationDate; // Date de terminaison
    private long numberOfOccurrences; // Nombre d'occurrences de répétition
    private boolean isFixedDate; // Indicateur pour savoir si la terminaison est par date ou par nombre d'occurrences

    /**
     * Constructeur pour une terminaison fixée à une date donnée.
     * @param start la date de début de l'événement
     * @param frequency la fréquence de répétition (jours, semaines, mois)
     * @param terminationInclusive la date de fin de l'événement
     */
    public Termination(LocalDate start, ChronoUnit frequency, LocalDate terminationInclusive) {
        this.terminationDate = terminationInclusive;
        this.numberOfOccurrences = -1; // -1 signifie "non défini"
        this.isFixedDate = true;
    }

    /**
     * Constructeur pour une terminaison fixée par un nombre d'occurrences.
     * @param start la date de début de l'événement
     * @param frequency la fréquence de répétition (jours, semaines, mois)
     * @param numberOfOccurrences le nombre d'occurrences de l'événement
     */
    public Termination(LocalDate start, ChronoUnit frequency, long numberOfOccurrences) {
        this.numberOfOccurrences = numberOfOccurrences;
        this.terminationDate = null;
        this.isFixedDate = false;
    }

    /**
     * Retourne la date de terminaison si elle est définie par une date précise.
     * @return la date de terminaison de l'événement
     */
    public LocalDate terminationDateInclusive() {
        if (isFixedDate) {
            return terminationDate;
        }
        throw new UnsupportedOperationException("La terminaison n'est pas définie par une date, mais par un nombre d'occurrences.");
    }

    /**
     * Retourne le nombre d'occurrences si la terminaison est définie par un nombre d'occurrences.
     * @return le nombre d'occurrences de l'événement
     */
    public long numberOfOccurrences() {
        if (!isFixedDate) {
            return numberOfOccurrences;
        }
        throw new UnsupportedOperationException("La terminaison n'est pas définie par un nombre d'occurrences, mais par une date.");
    }

    /**
     * Calcule la date de terminaison si l'événement est basé sur un nombre d'occurrences.
     * @param start la date de début de l'événement
     * @param frequency la fréquence de répétition
     * @return la date de terminaison
     */
    public LocalDate calculateTerminationDate(LocalDate start, ChronoUnit frequency) {
        if (!isFixedDate && numberOfOccurrences > 0) {
            return start.plus(numberOfOccurrences - 1, frequency);
        }
        throw new UnsupportedOperationException("La terminaison n'est pas définie par un nombre d'occurrences.");
    }
}

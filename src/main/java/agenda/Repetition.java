package agenda;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe représentant la répétition d'un événement.
 */
public class Repetition {

    private final ChronoUnit myFrequency; // Fréquence de répétition
    private List<LocalDate> exceptions; // Liste des exceptions
    private Termination termination; // Terminaison de la répétition

    /**
     * Constructeur pour définir la fréquence de la répétition.
     * @param myFrequency la fréquence de répétition (jours, semaines, mois)
     */
    public Repetition(ChronoUnit myFrequency) {
        this.myFrequency = myFrequency;
        this.exceptions = new ArrayList<>();
    }

    /**
     * Ajoute une exception à la répétition.
     * @param date la date où l'événement ne se répète pas
     */
    public void addException(LocalDate date) {
        if (!exceptions.contains(date)) {
            exceptions.add(date);
        }
    }

    /**
     * Définit la terminaison de la répétition.
     * @param termination l'objet Termination
     */
    public void setTermination(Termination termination) {
        this.termination = termination;
    }

    /**
     * Vérifie si l'événement se répète à une date donnée.
     * @param currentDate la date à vérifier
     * @param startDate la date de début de l'événement
     * @return true si l'événement se répète à la date donnée, false sinon
     */
    public boolean isRepeatedOnDate(LocalDate currentDate, LocalDate startDate) {
        // Vérifie si la date actuelle est dans les exceptions
        if (exceptions.contains(currentDate)) {
            return false;
        }

        // Vérifie si la date est dans la plage des répétitions
        if (termination != null) {
            if (termination.terminationDateInclusive() != null && currentDate.isAfter(termination.terminationDateInclusive())) {
                return false;
            }

            // Si la terminaison est définie par un nombre d'occurrences
            if (termination.numberOfOccurrences() > 0) {
                long daysBetween = ChronoUnit.DAYS.between(startDate, currentDate);
                return daysBetween % myFrequency.getDuration().toDays() == 0;
            }
        }
        return true;
    }

    /**
     * Retourne la fréquence de répétition
     * @return la fréquence de répétition
     */
    public ChronoUnit getFrequency() {
        return myFrequency;
    }
}

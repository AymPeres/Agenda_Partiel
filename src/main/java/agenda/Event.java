package agenda;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class Event {

    private String title;
    private LocalDateTime startDate;
    private Duration duration;

    // Champs pour gérer la répétition
    private ChronoUnit repetitionUnit;  // L'unité de répétition
    private LocalDate terminationDate;   // La date de terminaison
    private int numberOfOccurrences;    // Le nombre d'occurrences

    // Liste des exceptions
    private Set<LocalDate> exceptions = new HashSet<>();  // Utilisation d'un Set pour des vérifications rapides

    public Event(String title, LocalDateTime startDate, Duration duration) {
        this.title = title;
        this.startDate = startDate;
        this.duration = duration;
    }

    public void setRepetition(ChronoUnit repetitionUnit) {
        this.repetitionUnit = repetitionUnit;
    }

    public void setTermination(LocalDate terminationDate) {
        this.terminationDate = terminationDate;
    }

    public void setTermination(int numberOfOccurrences) {
        this.numberOfOccurrences = numberOfOccurrences;
        calculateTerminationDate();
    }

    private void calculateTerminationDate() {
        LocalDateTime currentDate = this.startDate;
        for (int i = 1; i < numberOfOccurrences; i++) {
            currentDate = currentDate.plus(1, repetitionUnit);
        }
        this.terminationDate = currentDate.toLocalDate();
    }

    public String getTitle() {
        return title;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    // Méthode pour obtenir la date de début
    public LocalDateTime getStart() {
        return startDate;
    }
    public Duration getDuration() {
        return duration;
    }

    // Retourner le nombre d'occurrences calculé en prenant en compte les exceptions
    public int getNumberOfOccurrences() {
        if (this.terminationDate != null) {
            LocalDateTime currentDate = this.startDate;
            int occurrences = 0;  // Initialisation à 0

            // Tant que la date actuelle ne dépasse pas la date de terminaison
            while (!currentDate.toLocalDate().isAfter(this.terminationDate)) {
                // Si la date n'est pas une exception
                if (!exceptions.contains(currentDate.toLocalDate())) {
                    occurrences++;  // Ajouter une occurrence valide
                }
                // Passer à l'occurrence suivante
                currentDate = currentDate.plus(1, repetitionUnit);
            }
            return occurrences;
        }
        // Si la terminaison n'est pas définie par date, utiliser le nombre d'occurrences donné
        return this.numberOfOccurrences;
    }

    public LocalDate getTerminationDate() {
        return this.terminationDate;
    }

    // Vérifie si l'événement se produit à une date donnée
    public boolean isInDay(LocalDate date) {
        // Vérifier d'abord si la date est avant le jour de début
        if (date.isBefore(this.startDate.toLocalDate())) {
            return false;
        }

        // Si aucune répétition n'est définie, vérification simple pour un événement unique
        if (repetitionUnit == null) {
            LocalDateTime startDateTime = this.startDate;
            LocalDateTime endDateTime = this.startDate.plus(this.duration);

            return (startDateTime.toLocalDate().equals(date) ||
                    (endDateTime.toLocalDate().equals(date) &&
                            !endDateTime.toLocalDate().equals(startDateTime.toLocalDate())))
                    && !exceptions.contains(date);
        }

        // Si la date de terminaison est nulle, on vérifie les occurrences sans limite
        if (this.terminationDate == null) {
            // Vérifier si la date est dans les exceptions
            if (exceptions.contains(date)) {
                return false;
            }

            // Calculer le nombre de répétitions depuis le début
            long daysBetween = ChronoUnit.DAYS.between(startDate.toLocalDate(), date);

            // Si l'unité de répétition n'est pas DAYS, on ne peut pas utiliser simplement daysBetween
            if (repetitionUnit != ChronoUnit.DAYS) {
                return false; // ou ajuster selon votre logique de répétition
            }

            // Vérifier si la date correspond à une occurrence
            return true;
        }

        // Pour les événements avec date de terminaison
        LocalDateTime currentDate = this.startDate;

        while (!currentDate.toLocalDate().isAfter(this.terminationDate)) {
            LocalDateTime occurrenceEnd = currentDate.plus(this.duration);

            // Vérifie si la date recherchée est comprise dans l'occurrence
            if ((currentDate.toLocalDate().equals(date) ||
                    (occurrenceEnd.toLocalDate().equals(date) &&
                            !occurrenceEnd.toLocalDate().equals(currentDate.toLocalDate())))
                    && !exceptions.contains(date)) {
                return true;
            }

            // Avancer à l'occurrence suivante
            currentDate = currentDate.plus(1, repetitionUnit);
        }

        return false;
    }

    @Override
    public String toString() {
        return "Event: " + title;
    }


    // Ajouter une exception (jour où l'événement ne doit pas se produire)
    public void addException(LocalDate exceptionDate) {
        exceptions.add(exceptionDate);
    }
}

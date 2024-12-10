package agenda;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Description : An agenda that stores events
 */
public class Agenda {
    ArrayList<Event> events = new ArrayList<>();
    /**
     * Adds an event to this agenda
     *
     * @param e the event to add
     */
    public void addEvent(Event e) {
        events.add(e);
    }

    /**
     * Computes the events that occur on a given day
     *
     * @param day the day toi test
     * @return a list of events that occur on that day
     */
    public List<Event> eventsInDay(LocalDate day) {
        ArrayList<Event> todayEvents = new ArrayList<Event>();
        for (Event e : events) {
            if (e.isInDay(day)){
                todayEvents.add(e);
            }
        }
        return todayEvents;
    }

    /**
     * Trouver les événements de l'agenda en fonction de leur titre
     * @param title le titre à rechercher
     * @return les événements qui ont le même titre
     */
    public List<Event> findByTitle(String title) {
        List<Event> sameTitleList = new ArrayList<Event>();
        for (Event e : events){
            if (e.getTitle().equals(title)){
                sameTitleList.add(e);
            }
        }
        return sameTitleList;
    }

    /**
     * Déterminer s’il y a de la place dans l'agenda pour un événement
     * @param e L'événement à tester (on se limitera aux événements simples)
     * @return vrai s’il y a de la place dans l'agenda pour cet événement
     */
    public boolean isFreeFor(Event e) {
        LocalDateTime eStart = e.getStart();
        LocalDateTime eEnd = e.getStart().plus(e.getDuration());
        for (Event event : events) {
            if ((eStart.isAfter(event.getStart()) && eStart.isBefore(event.getStart().plus(event.getDuration()))) || (eEnd.isAfter(event.getStart())) && eEnd.isBefore(event.getStart().plus(event.getDuration())) || (eStart.isAfter(event.getStart())) && eEnd.isAfter(event.getStart().plus(event.getDuration()))){
                return false;
            }
        }
        return true;
    }
}
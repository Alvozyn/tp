package seedu.address.model.event;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import seedu.address.model.event.exceptions.EventConflictException;
import seedu.address.model.event.exceptions.EventNotFoundException;
import seedu.address.model.person.Person;

/**
 * Represents the list of {@code RecurringEvent} that each {@code Person} has.
 */
public class RecurringEventList {
    private final TreeSet<RecurringEvent> recurringEvents = new TreeSet<>();

    public TreeSet<RecurringEvent> getRecurringEvents() {
        return recurringEvents;
    }

    /**
     * Gets the total number of event in the recurringEvents
     * @return the size of the recurringEvents
     */
    public int getSize() {
        return recurringEvents.size();
    }

    /**
     * Insert the recurring event object into the recurring event list
     * @param newEvent to be inserted
     */
    public void insert(RecurringEvent newEvent) {
        this.recurringEvents.add(newEvent);
    }

    public RecurringEvent checkClashingRecurringEvent(RecurringEvent recurringEvent) {
        Iterator<RecurringEvent> it = recurringEvents.iterator();
        RecurringEvent currEvent;

        int count = 0;
        while (it.hasNext()) {
            currEvent = it.next();

            if (recurringEvent.compareTo(currEvent) == 0) {
                return currEvent;
            }
            count++;
        }
        return null;
    }

    /**
     * Check if a recurring event exist within the recurring event list
     * @param event to be checked if exist
     * @return true if there exist a same event and false if the event does exist
     * in the event list
     */
    public boolean contain(RecurringEvent event) {
        return recurringEvents.contains(event);
    }

    /**
     * Delete the recurring event from the recurring event list.
     * @param event to be deleted.
     */
    public void deleteRecurringEvent(RecurringEvent event) {
        recurringEvents.remove(event);
    }

    /**
     * Get the recurring event in the recurring event list with the event's index.
     * @param index of the event.
     * @return recurringEventObject
     */
    public RecurringEvent getRecurringEvent(int index) {
        Iterator<RecurringEvent> it = recurringEvents.iterator();
        RecurringEvent recurringEvent = null;
        int counter = 0;
        while (it.hasNext()) {
            recurringEvent = it.next();
            if (counter == index) {
                break;
            }
            counter++;
        }
        return recurringEvent;
    }

    /**
     * Edit recurring event parameters in the recurring event list
     * @param originalEvent to be edited
     * @param editedRecurringEvent to be edited to
     */
    public void edit(RecurringEvent originalEvent, RecurringEvent editedRecurringEvent) {
        if (!recurringEvents.contains(originalEvent)) {
            throw new EventNotFoundException();
        }
        recurringEvents.remove(originalEvent);
        recurringEvents.add(editedRecurringEvent);
    }

    public void addAll(Set<RecurringEvent> recurringEvents) {
        this.recurringEvents.addAll(recurringEvents);
    }
    public ArrayList<RecurringEvent> getList() {
        return new ArrayList<>(this.recurringEvents);
    }
    public Set<RecurringEvent> getSet() {
        return new TreeSet<>(this.recurringEvents);
    }

    /**
     * Prints out a list of all event that occur within the given time period
     * @param startPeriod stand for the starting date of the time period
     * @param endPeriod stands for the ending date of the time period
     * @return a string of all events that occured within the time period
     */
    public String listBetweenOccurence(LocalDateTime startPeriod, LocalDateTime endPeriod) {
        StringBuilder output = new StringBuilder();
        for (RecurringEvent re : recurringEvents) {
            if (re.occursBetween(startPeriod, endPeriod)) {
                output.append(re).append("\n");
            }
        }
        return output.toString();
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder("Recurring Events\n");
        int count = 1;
        for (RecurringEvent re : recurringEvents) {
            output.append(count).append(". ").append(re.toString()).append("\n");
            count++;
        }
        return output.toString();
    }
}

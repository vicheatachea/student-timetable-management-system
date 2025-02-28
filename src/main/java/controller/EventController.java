package controller;

import dto.Event;
import model.EventModel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EventController {
    private final EventModel eventModel = new EventModel();

    // Fetch all events for a user within a time interval
    public List<Event> fetchEventsByUser(LocalDate startDate, LocalDate endDate) {
        // Placeholder
        return new ArrayList<>();
    }

    // Add an event for a user
    public void addEventForUser(Event event) {
        // Placeholder
    }

    // Update an event for a user
    public void updateEventForUser(Event event) {
        // Placeholder
    }

    // Delete an event for a user
    public void deleteEventForUser(Event event) {
        // Placeholder
    }

    // NOTE: groupName should be unique

    // Add an event for a group
    public void addEventForGroup(Event event, String groupName) {
        // Placeholder
    }

    // Update an event for a group
    public void updateEventForGroup(Event event, String groupName) {
        // Placeholder
    }

    // Delete an event for a group
    public void deleteEventForGroup(Event event, String groupName) {
        // Placeholder
    }
}

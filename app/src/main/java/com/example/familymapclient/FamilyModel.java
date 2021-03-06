package com.example.familymapclient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FamilyModel implements Serializable {

    private List<Person> persons = new ArrayList<>();
    private List<Event> events = new ArrayList<>();
    private String currentUser;

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public List<Person> getPersons() {
        return persons;
    }

    public Person getPerson(String personID) {
        Person person = null;

        for (Person p : persons) {
            if (p.getPersonID().equals(personID)) {
                person = p;
                break;
            }
        }

        return person;
    }

    public void addPerson(Person person) {
        persons.add(person);
    }

    public List<Event> getEvents() {
        return events;
    }

    public List<Event> getEvents(FiltersData filtersData) {
        if (filtersData == null) {
            return events;
        }

        List<Event> filteredEvents = new ArrayList<>();
        Map<String, Boolean> filters = filtersData.filters;

        for (Event event : events) {
            if (!filters.containsKey(event.getEventType()) || filters.get(event.getEventType())) {
                filteredEvents.add(event);
            }
        }

        Person currentPerson = getPerson(getCurrentUser());

        if (filters.containsKey("father") && !filters.get("father")) {
            filteredEvents = filterParentSide(filteredEvents, currentPerson.getFather());
        }

        if (filters.containsKey("mother") && !filters.get("mother")) {
            filteredEvents = filterParentSide(filteredEvents, currentPerson.getMother());
        }

        if (filters.containsKey("male") && !filters.get("male")) {
            filteredEvents = filterGender(filteredEvents, "m");
        }

        if (filters.containsKey("female") && !filters.get("female")) {
            filteredEvents = filterGender(filteredEvents, "f");
        }

        return filteredEvents;
    }

    private List<Event> filterGender(List<Event> events, String gender) {
        List<Event> filteredEvents = new ArrayList<>();

        for (Event event : events) {
            Person person = getPerson(event.getPersonID());
            if (!person.getGender().equals(gender)) {
                filteredEvents.add(event);
            }
        }

        return filteredEvents;
    }

    private List<Event> filterParentSide(List<Event> events, String personId) {
        if (personId == null || personId.equals("null")) {
            return events;
        }

        List<Event> filteredEvents = new ArrayList<>();

        for (Event event : events) {
            if (!event.getPersonID().equals(personId)) {
                filteredEvents.add(event);
            }
        }

        Person person = getPerson(personId);

        filteredEvents = filterParentSide(filteredEvents, person.getFather());
        filteredEvents = filterParentSide(filteredEvents, person.getMother());

        return filteredEvents;
    }

    public Event getEvent(String eventId) {
        Event e = null;

        for (Event event : events) {
            if (event.getEventID().equals(eventId)) {
                e = event;
                break;
            }
        }

        return e;
    }

    public void addEvent(Event event) {
        events.add(event);
    }

    public List<Event> getEvents(String personId) {
        List<Event> list = new ArrayList<>();

        for (Event event : events) {
            if (event.getPersonID().equals(personId)) {
                list.add(event);
            }
        }

        return list;
    }

    public List<Event> getEvents(String personId, FiltersData filtersData) {
        List<Event> filteredList = new ArrayList<>();

        for (Event event : this.getEvents(filtersData)) {
            if (event.getPersonID().equals(personId)) {
                filteredList.add(event);
            }
        }

        return filteredList;
    }

    static public class Person implements Serializable {
        /** Person primary key */
        private String personID;
        /** Descendant */
        private String descendant;
        /** First name */
        private String firstName;
        /** Last name */
        private String lastName;
        /** Person gender */
        private String gender;
        /** Father name */
        private String father;
        /** Mother name */
        private String mother;
        /** Spouse name */
        private String spouse;

        public Person(String personID, String firstName, String lastName, String gender) {
            this.personID = personID;
            this.firstName = firstName;
            this.lastName = lastName;
            this.gender = gender;
        }

        public String getPersonID() {
            return personID;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public String getGender() {
            return gender;
        }

        public String getDescendant() {
            return descendant;
        }

        public void setDescendant(String descendant) {
            this.descendant = descendant;
        }

        public String getFather() {
            return father;
        }

        public void setFather(String father) {
            this.father = father;
        }

        public String getMother() {
            return mother;
        }

        public void setMother(String mother) {
            this.mother = mother;
        }

        public String getSpouse() {
            return spouse;
        }

        public void setSpouse(String spouse) {
            this.spouse = spouse;
        }
    }

    static public class Event implements Serializable {
        /** Event ID */
        private String eventID;
        /** Desendant */
        private String descendant;
        /** Person object */
        private String personID;
        /** Location latitude */
        private double latitude;
        /** Location longitude */
        private double longitude;
        /** Location country */
        private String country;
        /** Location city */
        private String city;
        /** Event type */
        private String eventType;
        /** Event year */
        private int year;

        public Event(String eventID, String descendant, String personID, double latitude, double longitude,
                     String country, String city, String eventType, int year) {
            this.eventID = eventID;
            this.descendant = descendant;
            this.personID = personID;
            this.latitude = latitude;
            this.longitude = longitude;
            this.country = country;
            this.city = city;
            this.eventType = eventType;
            this.year = year;
        }

        public String getEventID() {
            return eventID;
        }

        public void setEventID(String eventID) {
            this.eventID = eventID;
        }

        public String getDescendant() {
            return descendant;
        }

        public void setDescendant(String descendant) {
            this.descendant = descendant;
        }

        public String getPersonID() {
            return personID;
        }

        public void setPersonID(Person person) {
            this.personID = personID;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(float latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(float longitude) {
            this.longitude = longitude;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getEventType() {
            return eventType;
        }

        public void setEventType(String eventType) {
            this.eventType = eventType;
        }

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }
    }
}


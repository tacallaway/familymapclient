package com.example.familymapclient;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.*;

public class FamilyModelTest {

    FamilyModel familyModel;

    @Before
    public void setUp() throws Exception {
        familyModel = new FamilyModel();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void setCurrentUser() {
        familyModel.setCurrentUser("test");

        assertEquals("test", familyModel.getCurrentUser());
    }

    @Test
    public void getCurrentUser() {
        familyModel.setCurrentUser("test");

        assertEquals("test", familyModel.getCurrentUser());
    }

    @Test
    public void getCurrentUserFailure() {
        familyModel.setCurrentUser("test");

        assertNotEquals("dummy", familyModel.getCurrentUser());
    }

    @Test
    public void getPersons() {
        familyModel.addPerson(createPerson());
        familyModel.addPerson(createPerson());
        familyModel.addPerson(createPerson());

        assertEquals(3, familyModel.getPersons().size());
    }

    @Test
    public void getPerson() {
        FamilyModel.Person person = createPerson();
        familyModel.addPerson(person);

        FamilyModel.Person person2 = familyModel.getPerson(person.getPersonID());
        assertNotNull(person2);
        assertEquals(person.getPersonID(), person2.getPersonID());
    }

    @Test
    public void addPerson() {
        FamilyModel.Person person = createPerson();
        familyModel.addPerson(person);

        assertEquals(1, familyModel.getPersons().size());
        assertNotNull(familyModel.getPerson(person.getPersonID()));
    }

    @Test
    public void getEvents() {
        familyModel.addEvent(createEvent());
        familyModel.addEvent(createEvent());
        familyModel.addEvent(createEvent());

        assertEquals(3, familyModel.getEvents().size());
    }

    @Test
    public void getEvent() {
        FamilyModel.Event event = createEvent();
        familyModel.addEvent(event);

        FamilyModel.Event event2 = familyModel.getEvent(event.getEventID());
        assertNotNull(event2);
        assertEquals(event.getEventID(), event2.getEventID());
    }

    @Test
    public void addEvent() {
        FamilyModel.Event event = createEvent();
        familyModel.addEvent(event);

        assertEquals(1, familyModel.getEvents().size());
        assertNotNull(familyModel.getEvent(event.getEventID()));
    }

    private FamilyModel.Person createPerson() {
        return new FamilyModel.Person(UUID.randomUUID().toString(), "first", "last", "m");
    }

    private FamilyModel.Event createEvent() {
        return new FamilyModel.Event(UUID.randomUUID().toString(), "test", UUID.randomUUID().toString(), 1, 1, "country", "city", "event", 1900);
    }
}
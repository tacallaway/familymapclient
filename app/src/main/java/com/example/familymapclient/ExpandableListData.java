package com.example.familymapclient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListData {


    public static HashMap<String, List<String>> getData(String personId, FamilyModel familyModel) {

        FamilyModel.Person person = familyModel.getPerson(personId);

        HashMap<String, List<String>> expandableListDetail = new HashMap<>();

        List<String> lifeEvents = new ArrayList<>();
        for (FamilyModel.Event event : familyModel.getEvents(personId)) {
            lifeEvents.add(event.getEventID() + "|" + event.getEventType() + ": " + event.getCity() + ", " + event.getCountry() + "\n" + person.getFirstName() + " " + person.getLastName());
        }

        List<String> family = new ArrayList<>();
        if (!person.getFather().equals("null")) {
            FamilyModel.Person father = familyModel.getPerson(person.getFather());
            family.add(father.getPersonID() + "|" + father.getGender() + "|" + father.getFirstName() + " " + father.getLastName() + "\nFather");
        }
        if (!person.getMother().equals("null")) {
            FamilyModel.Person mother = familyModel.getPerson(person.getMother());
            family.add(mother.getPersonID() + "|" + mother.getGender() + "|" + mother.getFirstName() + " " + mother.getLastName() + "\nMother");
        }
        if (!person.getSpouse().equals("null")) {
            FamilyModel.Person spouse = familyModel.getPerson(person.getSpouse());
            family.add(spouse.getPersonID() + "|" + spouse.getGender() + "|" + spouse.getFirstName() + " " + spouse.getLastName() + "\nSpouse");
        }

        String currentPersonId = person.getPersonID();
        for (FamilyModel.Person p : familyModel.getPersons()) {
            if (p.getFather().equals(currentPersonId) || p.getMother().equals(currentPersonId)) {
                family.add(p.getPersonID() + "|" + p.getGender() + "|" + p.getFirstName() + " " + p.getLastName() + "\nChild");
            }
        }

        expandableListDetail.put("LIFE EVENTS", lifeEvents);
        expandableListDetail.put("FAMILY", family);
        return expandableListDetail;
    }
}
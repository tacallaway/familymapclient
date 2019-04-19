package com.example.familymapclient;

import java.util.ArrayList;
import java.util.List;

public class SearchListData {
    public static List<String> getData(String searchString, FamilyModel familyModel) {

        searchString = searchString.toLowerCase();
        List<String> searchListData = new ArrayList<>();

        for (FamilyModel.Person person : familyModel.getPersons()) {
            if (person.getFirstName().toLowerCase().contains(searchString) || person.getLastName().toLowerCase().contains(searchString)) {
                searchListData.add("person|" + person.getGender() + "|" + person.getFirstName() + " " + person.getLastName());
            }
        }

        for (FamilyModel.Event event : familyModel.getEvents()) {
            if (event.getCity().toLowerCase().contains(searchString) || event.getCountry().toLowerCase().contains(searchString)) {
                FamilyModel.Person person = familyModel.getPerson(event.getPersonID());
                searchListData.add("event|" + event.getCity() + ", " + event.getCountry() + "|" + person.getFirstName() + " " + person.getLastName());
            }
        }

        return searchListData;
    }

}

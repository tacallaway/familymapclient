package com.example.familymapclient;

import java.util.ArrayList;
import java.util.List;

public class FilterListData {
    public static List<String> getData(FamilyModel familyModel) {

        List<String> filterListData = new ArrayList<>();
        List<String> eventTypes = new ArrayList<>();

        for (FamilyModel.Event event : familyModel.getEvents()) {
            String eventType = event.getEventType();
            if (!eventTypes.contains(eventType)) {
                eventTypes.add(eventType);
                filterListData.add(eventType + "|" + eventType.substring(0,1).toUpperCase() + eventType.substring(1).toLowerCase() + " Events|FILTER BY " + eventType.toUpperCase() + " EVENTS");
            }
        }

        filterListData.add("father|Father's Side|FILTER BY FATHER'S SIDE OF FAMILY");
        filterListData.add("mother|Mother's Side|FILTER BY MOTHER'S SIDE OF FAMILY");
        filterListData.add("male|Male Events|FILTER EVENTS BASED ON GENDER");
        filterListData.add("female|Female Events|FILTER EVENTS BASED ON GENDER");

        return filterListData;
    }
}

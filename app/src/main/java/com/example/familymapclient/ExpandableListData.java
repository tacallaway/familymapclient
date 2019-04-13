package com.example.familymapclient;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListData {


    public static HashMap<String, List<String>> getData(String personId, FamilyModel familyModel) {


        HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();

        List<String> lifeEvents = new ArrayList<String>();
        for (FamilyModel.Event event : familyModel.getEvents(personId)) {
            lifeEvents.add(event.getEventType() + ": " + event.getCity() + ", " + event.getCountry());
        }

        List<String> family = new ArrayList<String>();
        FamilyModel.Person person = familyModel.getPerson(personId);
        if (!person.getFather().equals("null")) {
            FamilyModel.Person father = familyModel.getPerson(person.getFather());
            family.add(father.getGender() + "|" + father.getFirstName() + " " + father.getLastName() + "\nFather");
        }
        if (!person.getMother().equals("null")) {
            FamilyModel.Person mother = familyModel.getPerson(person.getMother());
            family.add(mother.getGender() + "|" + mother.getFirstName() + " " + mother.getLastName() + "\nMother");
        }
        if (!person.getSpouse().equals("null")) {
            FamilyModel.Person spouse = familyModel.getPerson(person.getSpouse());
            family.add(spouse.getGender() + "|" + spouse.getFirstName() + " " + spouse.getLastName() + "\nSpouse");
        }

        expandableListDetail.put("LIFE EVENTS", lifeEvents);
        expandableListDetail.put("FAMILY", family);
        return expandableListDetail;
    }
}
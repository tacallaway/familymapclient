package com.example.familymapclient;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class FiltersData implements Serializable {
    public Map<String, Boolean> filters = new HashMap<>();
    public String currentUser;
}

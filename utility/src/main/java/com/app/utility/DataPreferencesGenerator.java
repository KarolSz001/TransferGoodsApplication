package com.app.utility;

import com.app.converter.PreferencesToJsonConverter;
import com.app.enums.Category;
import com.app.model.Preference;

import java.util.List;

public class DataPreferencesGenerator {

    private final String fileNamePreferences = "jsonFilePreferences.json";

    private final List<Preference> preferences = List.of(
            new Preference(1, Category.AGD),
            new Preference(2, Category.ELECTRONICS));
//            new Preference(3, Category.BOOKS));
//            new Preference(4, Category.GROCERIES));


    public DataPreferencesGenerator() {
        initializationPreferencesJsonFile();
    }

    public List<Preference> getPreferences() {
        return preferences;
    }

    private void initializationPreferencesJsonFile() {
        PreferencesToJsonConverter preferencesToJsonConverter = new PreferencesToJsonConverter(fileNamePreferences);
        preferencesToJsonConverter.toJson(preferences);
    }




}

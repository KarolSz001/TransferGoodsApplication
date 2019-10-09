package com.app.service;

import com.app.converter.PreferencesToJsonConverter;
import com.app.exception.MyUncheckedException;
import com.app.model.Preference;

import java.util.List;

public class PreferenceService {

    private final List<Preference> preferences;
    private final PreferencesToJsonConverter preferencesToJsonConverter;

    public PreferenceService(String filename) {
        preferencesToJsonConverter = new PreferencesToJsonConverter(filename);
        preferences = loadPreferencesFromJsonFile();
    }

    public List<Preference> loadPreferencesFromJsonFile() {
        return preferencesToJsonConverter.fromJson().orElseThrow(() -> new MyUncheckedException("LOAD PREFERENCES ERROR"));
    }

    public List<Preference> findAll() {
        return preferences;
    }

    public int getNumberOfAllPreferences() {
        return preferences.size();
    }
}




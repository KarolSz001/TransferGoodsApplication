package com.app.converter;

import com.app.model.Preference;

import java.util.List;

public class PreferencesToJsonConverter extends JsonConverter<List<Preference>> {
    public PreferencesToJsonConverter(String jsonFilename) {
        super(jsonFilename);
    }
}

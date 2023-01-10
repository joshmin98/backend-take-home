package org.gremlin.models;

import lombok.NonNull;

public enum Language {
    English("en"),
    Russian("ru");

    private final String countryCode;

    Language(@NonNull final String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryCode() {
        return this.countryCode;
    }
}

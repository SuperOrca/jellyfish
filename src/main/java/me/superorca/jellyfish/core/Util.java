package me.superorca.jellyfish.core;

import lombok.experimental.UtilityClass;

import java.util.Locale;

@UtilityClass
public class Util {
    public String getCountryName(String countryId) {
        return new Locale("", countryId).getDisplayCountry();
    }
}

package com.wiedzminjelon.coronavirustrackerapp.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationStats {
    private String state;
    private String country;
    private int latestTotalCases;

    @Override
    public String toString() {
        return "State: " + state + " country " + country + "latest total cases was " + latestTotalCases;
    }
}

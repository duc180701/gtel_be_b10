package com.gtel.srpingtutorial.model.request;

import lombok.Data;

@Data
public class AirportRequest {
    private String iata;
    private String name;
    private String airportGroupCode;
    private String language;
    private Integer priority;
    private LocationRequest location;

    public String getIata() {
        return iata;
    }

    public void setIata(String iata) {
        this.iata = iata;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAirportGroupCode() {
        return airportGroupCode;
    }

    public void setAirportGroupCode(String airportGroupCode) {
        this.airportGroupCode = airportGroupCode;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public LocationRequest getLocation() {
        return location;
    }

    public void setLocation(LocationRequest location) {
        this.location = location;
    }
}

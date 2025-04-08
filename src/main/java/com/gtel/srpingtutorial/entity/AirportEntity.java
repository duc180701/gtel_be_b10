package com.gtel.srpingtutorial.entity;

import com.gtel.srpingtutorial.model.request.AirportRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "airport")
public class AirportEntity extends BaseEntity{

    @Id
    private String iata;

    @Column(name = "name")
    private String name;

    @Column(name = "airportgroupcode")
    private String airportgroupcode;

    @Column(name = "language")
    private String language;

    @Column(name = "priority")
    private Integer priority;

    public AirportEntity(){

    }


    public AirportEntity(AirportRequest request){
        this.airportgroupcode = request.getAirportGroupCode();
        this.name = request.getName();
        this.iata = request.getIata();
        this.language = request.getLanguage();
        this.priority = request.getPriority();
    }

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

    public String getAirportgroupcode() {
        return airportgroupcode;
    }

    public void setAirportgroupcode(String airportgroupcode) {
        this.airportgroupcode = airportgroupcode;
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
}

package com.parkinglotmanagement;

import java.util.Calendar;


public class BookedProperties {
    String name,email,plate;
    double fees;
    String from,to;

    public BookedProperties()
    {

    }

    public BookedProperties(String name, String email, String plate, double fees, String from, String to) {
        this.name = name;
        this.email = email;
        this.plate = plate;
        this.fees = fees;
        this.from = from;
        this.to = to;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public double getFees() {
        return fees;
    }

    public void setFees(double fees) {
        this.fees = fees;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}

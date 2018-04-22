package com.parkinglotmanagement;

public class MyBookingBean {
    String slot,plate,from,to;
    double fees;
    public MyBookingBean(String slot, String plate, String from, String to, double fees) {
        this.slot = slot;
        this.plate = plate;
        this.from = from;
        this.to = to;
        this.fees = fees;
    }

    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
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

    public double getFees() {
        return fees;
    }

    public void setFees(double fees) {
        this.fees = fees;
    }
}

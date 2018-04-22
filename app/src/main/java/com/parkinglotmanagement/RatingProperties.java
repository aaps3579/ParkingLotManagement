package com.parkinglotmanagement;

/**
 * Created by HP_PC on 05-04-2018.
 */

public class RatingProperties {
    float stars;
    String text;

    public RatingProperties() {
    }

    public RatingProperties(float stars, String text) {
        this.stars = stars;
        this.text = text;
    }

    public float getStars() {
        return stars;
    }

    public void setStars(float stars) {
        this.stars = stars;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

package com.Application.khanapina.ModelClass;

public class Restaurant_info {
    private String name;
    private String cuisines;
    private String votes;
    private String rating_text;
    private String aggregate_rating;
    private String photos_url;
    private String rating_color;


    public Restaurant_info() {
    }

    public Restaurant_info(String name, String cuisines, String votes, String rating_text, String aggregate_rating,String photos_url,String rating_color) {
        this.name = name;
        this.cuisines = cuisines;
        this.votes = votes;
        this.rating_text = rating_text;
        this.aggregate_rating = aggregate_rating;
        this.photos_url=photos_url;
        this.rating_color=rating_color;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCuisines() {
        return cuisines;
    }

    public void setCuisines(String cuisines) {
        this.cuisines = cuisines;
    }

    public String getVotes() {
        return votes;
    }

    public void setVotes(String votes) {
        this.votes = votes;
    }

    public String getRating_text() {
        return rating_text;
    }

    public void setRating_text(String rating_text) {
        this.rating_text = rating_text;
    }

    public String getAggregate_rating() {
        return aggregate_rating;
    }

    public void setAggregate_rating(String aggregate_rating) {
        this.aggregate_rating = aggregate_rating;
    }

    public String getPhotos_url() {
        return photos_url;
    }

    public void setPhotos_url(String photos_url) {
        this.photos_url = photos_url;
    }

    public String getRating_color() {
        return rating_color;
    }

    public void setRating_color(String rating_color) {
        this.rating_color = rating_color;
    }
}

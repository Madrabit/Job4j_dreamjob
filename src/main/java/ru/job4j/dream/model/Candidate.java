package ru.job4j.dream.model;

import java.util.Objects;

/**
 * @author madrabit on 21.04.2020
 * @version 1$
 * @since 0.1
 */
public class Candidate {
    private int id;
    private String name;
    private String lastName;
    private String country;
    private String region;
    private String city;
    private String sex = "male";
    private String description;
    private String photoId;

    public Candidate(int id, String name, String lastName, String country, String region, String city, String sex, String description, String photoId) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.country = country;
        this.region = region;
        this.city = city;
        this.sex = sex;
        this.description = description;
        this.photoId = photoId;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoId() {
        return photoId;
    }

    public void setPhotoId(String photoId) {
        this.photoId = photoId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Candidate candidate = (Candidate) o;
        return id == candidate.id &&
                Objects.equals(name, candidate.name) &&
                Objects.equals(lastName, candidate.lastName) &&
                Objects.equals(sex, candidate.sex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, lastName, sex);
    }
}

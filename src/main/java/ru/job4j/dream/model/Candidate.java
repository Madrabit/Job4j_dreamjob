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
    private String photoId;

    public Candidate(int id, String name, String photoId) {
        this.id = id;
        this.name = name;
        this.photoId = photoId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Candidate candidate = (Candidate) o;
        return id == candidate.id &&
                name.equals(candidate.name) &&
                photoId.equals(candidate.photoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}

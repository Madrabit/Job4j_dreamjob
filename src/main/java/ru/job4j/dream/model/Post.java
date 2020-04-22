package ru.job4j.dream.model;

import java.time.LocalDate;
import java.util.Objects;

/**
 * @author madrabit on 18.04.2020
 * @version 1$
 * @since 0.1
 */
public class Post {
    private int id;
    private String name;
    private String description;
    LocalDate created;

    public Post(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        created = LocalDate.now();
    }

    public Post(int i, String name) {
        this.id = i;
        this.name = name;
        created = LocalDate.now();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getCreated() {
        return created;
    }

    public void setCreated(LocalDate created) {
        this.created = created;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return id == post.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

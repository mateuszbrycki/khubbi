package com.bookkeeper.app.application.domain.model;

import java.io.File;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

public class Photo {
    private final UUID id;
    private final String description;
    private final File photo;
    private final ZonedDateTime date;
    private final User owner;


    public Photo(UUID id, String description, File photo, ZonedDateTime date, User owner) {
        this.id = id;
        this.description = description;
        this.photo = photo;
        this.date = date;
        this.owner = owner;
    }

    public Photo(String description, File photo, ZonedDateTime date, User owner) {
        this.id = UUID.randomUUID();
        this.description = description;
        this.photo = photo;
        this.date = date;
        this.owner = owner;
    }

    public UUID getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public File getPhoto() {
        return photo;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public User getOwner() {
        return owner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Photo photo1 = (Photo) o;
        return Objects.equals(id, photo1.id) && Objects.equals(description, photo1.description) && Objects.equals(photo, photo1.photo) && Objects.equals(date, photo1.date) && Objects.equals(owner, photo1.owner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, photo, date, owner);
    }
}

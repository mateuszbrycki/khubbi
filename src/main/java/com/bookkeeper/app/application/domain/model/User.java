package com.bookkeeper.app.application.domain.model;

import java.util.Date;
import java.util.UUID;

public class User {

    private UUID id;

    private String fullName;

    private String email;

    private String password;

    private Date createdAt;

    private Date updatedAt;

    public User(UUID id, String fullName, String email, String password, Date createdAt, Date updatedAt) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }
}

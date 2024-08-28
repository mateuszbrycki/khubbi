package com.bookkeeper.app.application.domain.model;

import java.util.Date;
import java.util.UUID;
import lombok.Builder;
import lombok.With;

@With
@Builder
public record User( // FIXME mateusz.brycki introduce ValueObjects
    UUID id, String fullName, String email, String password, Date createdAt, Date updatedAt) {}

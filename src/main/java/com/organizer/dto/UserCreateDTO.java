package com.organizer.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class UserCreateDTO {

    private String email;

    private String passwordHash;

    private String fullName;

    private Boolean isActive;

    private LocalDateTime createdAt = LocalDateTime.now();

    private Set<Role> roles = new HashSet<>();
}
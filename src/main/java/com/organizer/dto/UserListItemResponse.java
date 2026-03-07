package com.organizer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class UserListItemResponse {
    private Long id;
    private String email;
    private String fullName;
    private Boolean isActive;
    private List<String> roles;
}
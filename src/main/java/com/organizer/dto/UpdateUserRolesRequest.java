package com.organizer.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UpdateUserRolesRequest {
    private Set<Long> roleIds;
}
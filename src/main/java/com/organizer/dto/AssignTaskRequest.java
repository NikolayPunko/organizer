package com.organizer.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AssignTaskRequest {
    private Long weddingId;
    private String title;
    private Long memberId;
    private LocalDate dueDate;
}
package com.organizer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DashboardResponse {

    private long weddingsCount;
    private long clientsCount;
    private long overdueTasksCount;
    private long meetingsCount;
    private long paymentsCount;
}
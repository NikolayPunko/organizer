package com.organizer.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class WeddingDetailsResponse {

    private Long id;
    private LocalDate weddingDate;
    private String status;
    private BigDecimal budgetTotal;

    private ClientShort client;

    private List<TaskShort> tasks;
    private List<MeetingShort> meetings;
    private List<ContractShort> contracts;
    private List<PaymentShort> payments;
    private List<WeddingTeamShort> team;

    @Getter
    @Setter
    public static class ClientShort {
        private Long id;
        private String name1;
        private String name2;
        private String phone;
        private String email;
    }

    @Getter
    @Setter
    public static class TaskShort {
        private Long id;
        private String title;
        private String status;
        private LocalDate dueDate;
        private String assignedTo;
    }

    @Getter
    @Setter
    public static class MeetingShort {
        private Long id;
        private String meetingDate;
        private String location;
        private String notes;
    }

    @Getter
    @Setter
    public static class ContractShort {
        private Long id;
        private String serviceName;
        private BigDecimal agreedPrice;
        private String status;
    }

    @Getter
    @Setter
    public static class PaymentShort {
        private Long id;
        private BigDecimal amount;
        private String paymentType;
        private LocalDate paymentDate;
    }

    @Getter
    @Setter
    public static class WeddingTeamShort {
        private Long id;
        private String role;
        private String memberName;
        private String position;
    }
}
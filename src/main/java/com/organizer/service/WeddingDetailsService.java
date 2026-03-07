package com.organizer.service;

import com.organizer.dto.WeddingDetailsResponse;
import com.organizer.entity.*;
import com.organizer.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WeddingDetailsService {

    private final WeddingRepository weddingRepository;
    private final TaskRepository taskRepository;
    private final MeetingRepository meetingRepository;
    private final ContractRepository contractRepository;
    private final PaymentRepository paymentRepository;
    private final WeddingTeamRepository weddingTeamRepository;

    public WeddingDetailsResponse getDetails(Long weddingId) {
        Wedding wedding = weddingRepository.findById(weddingId)
                .orElseThrow(() -> new RuntimeException("Wedding not found"));

        WeddingDetailsResponse response = new WeddingDetailsResponse();
        response.setId(wedding.getId());
        response.setWeddingDate(wedding.getWeddingDate());
        response.setStatus(wedding.getStatus());
        response.setBudgetTotal(wedding.getBudgetTotal());

        WeddingDetailsResponse.ClientShort client = new WeddingDetailsResponse.ClientShort();
        client.setId(wedding.getClient().getId());
        client.setName1(wedding.getClient().getName1());
        client.setName2(wedding.getClient().getName2());
        client.setPhone(wedding.getClient().getPhone());
        client.setEmail(wedding.getClient().getEmail());
        response.setClient(client);

        response.setTasks(
                taskRepository.findByWeddingId(weddingId).stream().map(this::mapTask).collect(Collectors.toList())
        );

        response.setMeetings(
                meetingRepository.findByWeddingId(weddingId).stream().map(this::mapMeeting).collect(Collectors.toList())
        );

        response.setContracts(
                contractRepository.findByWeddingId(weddingId).stream().map(this::mapContract).collect(Collectors.toList())
        );

        response.setPayments(
                paymentRepository.findByWeddingId(weddingId).stream().map(this::mapPayment).collect(Collectors.toList())
        );

        response.setTeam(
                weddingTeamRepository.findByWeddingId(weddingId).stream().map(this::mapTeam).collect(Collectors.toList())
        );

        return response;
    }

    private WeddingDetailsResponse.TaskShort mapTask(Task task) {
        WeddingDetailsResponse.TaskShort dto = new WeddingDetailsResponse.TaskShort();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setStatus(task.getStatus());
        dto.setDueDate(task.getDueDate());
        dto.setAssignedTo(task.getAssignedTo() != null ? task.getAssignedTo().getFullName() : null);
        return dto;
    }

    private WeddingDetailsResponse.MeetingShort mapMeeting(Meeting meeting) {
        WeddingDetailsResponse.MeetingShort dto = new WeddingDetailsResponse.MeetingShort();
        dto.setId(meeting.getId());
        dto.setMeetingDate(meeting.getMeetingDate() != null ? meeting.getMeetingDate().toString() : null);
        dto.setLocation(meeting.getLocation());
        dto.setNotes(meeting.getNotes());
        return dto;
    }

    private WeddingDetailsResponse.ContractShort mapContract(Contract contract) {
        WeddingDetailsResponse.ContractShort dto = new WeddingDetailsResponse.ContractShort();
        dto.setId(contract.getId());
        dto.setServiceName(contract.getService() != null ? contract.getService().getServiceName() : null);
        dto.setAgreedPrice(contract.getAgreedPrice());
        dto.setStatus(contract.getStatus());
        return dto;
    }

    private WeddingDetailsResponse.PaymentShort mapPayment(Payment payment) {
        WeddingDetailsResponse.PaymentShort dto = new WeddingDetailsResponse.PaymentShort();
        dto.setId(payment.getId());
        dto.setAmount(payment.getAmount());
        dto.setPaymentType(payment.getPaymentType());
        dto.setPaymentDate(payment.getPaymentDate());
        return dto;
    }

    private WeddingDetailsResponse.WeddingTeamShort mapTeam(WeddingTeam team) {
        WeddingDetailsResponse.WeddingTeamShort dto = new WeddingDetailsResponse.WeddingTeamShort();
        dto.setId(team.getId());
        dto.setRole(team.getRole());
        dto.setMemberName(team.getMember() != null ? team.getMember().getFullName() : null);
        dto.setPosition(team.getMember() != null ? team.getMember().getPosition() : null);
        return dto;
    }
}
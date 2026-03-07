package com.organizer.service;

import com.organizer.dto.AddVendorContractRequest;
import com.organizer.dto.AssignTaskRequest;
import com.organizer.dto.CreateWeddingRequest;
import com.organizer.dto.RegisterPaymentRequest;
import com.organizer.repository.ProcedureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProcedureService {

    private final ProcedureRepository procedureRepository;

    public void createWedding(CreateWeddingRequest request) {
        procedureRepository.createWedding(
                request.getClientId(),
                request.getWeddingDate(),
                request.getBudget(),
                request.getUserId()
        );
    }

    public void closeWedding(Long weddingId) {
        procedureRepository.closeWedding(weddingId);
    }

    public void addVendorContract(AddVendorContractRequest request) {
        procedureRepository.addVendorContract(
                request.getWeddingId(),
                request.getServiceId(),
                request.getPrice()
        );
    }

    public void registerPayment(RegisterPaymentRequest request) {
        procedureRepository.registerPayment(
                request.getWeddingId(),
                request.getContractId(),
                request.getAmount(),
                request.getPaymentType()
        );
    }

    public void assignTask(AssignTaskRequest request) {
        procedureRepository.assignTask(
                request.getWeddingId(),
                request.getTitle(),
                request.getMemberId(),
                request.getDueDate()
        );
    }

    public void completeTask(Long taskId) {
        procedureRepository.completeTask(taskId);
    }

    public void generateInitialTasks(Long weddingId) {
        procedureRepository.generateInitialTasks(weddingId);
    }
}
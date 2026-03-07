package com.organizer.service;

import com.organizer.entity.Contract;
import com.organizer.repository.ContractRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ContractService {

    private final ContractRepository contractRepository;

    public List<Contract> findAll() {
        return contractRepository.findAll();
    }

    public Optional<Contract> findById(Long id) {
        return contractRepository.findById(id);
    }

    public List<Contract> findByWeddingId(Long weddingId) {
        return contractRepository.findByWeddingId(weddingId);
    }

    public List<Contract> findByStatus(String status) {
        return contractRepository.findByStatus(status);
    }

    public Contract save(Contract contract) {
        return contractRepository.save(contract);
    }

    public Contract update(Long id, Contract updatedContract) {
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contract not found"));

        contract.setAgreedPrice(updatedContract.getAgreedPrice());
        contract.setContractDate(updatedContract.getContractDate());
        contract.setStatus(updatedContract.getStatus());
        contract.setWedding(updatedContract.getWedding());
        contract.setService(updatedContract.getService());

        return contractRepository.save(contract);
    }

    public void delete(Long id) {
        contractRepository.deleteById(id);
    }
}
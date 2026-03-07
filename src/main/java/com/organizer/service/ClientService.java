package com.organizer.service;

import com.organizer.entity.Client;
import com.organizer.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    public Optional<Client> findById(Long id) {
        return clientRepository.findById(id);
    }

    public Client save(Client client) {
        return clientRepository.save(client);
    }

    public Client update(Long id, Client updatedClient) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found"));

        client.setName1(updatedClient.getName1());
        client.setName2(updatedClient.getName2());
        client.setPhone(updatedClient.getPhone());
        client.setEmail(updatedClient.getEmail());
        client.setCreatedAt(updatedClient.getCreatedAt());

        return clientRepository.save(client);
    }

    public void delete(Long id) {
        clientRepository.deleteById(id);
    }
}
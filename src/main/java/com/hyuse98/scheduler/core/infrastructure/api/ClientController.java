package com.hyuse98.scheduler.core.infrastructure.api;

import com.hyuse98.scheduler.core.application.dto.ClientDto;
import com.hyuse98.scheduler.core.application.dto.UpdateClientRequest;
import com.hyuse98.scheduler.core.application.usecases.client.*;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/client")
public class ClientController {

    private final SaveClientUseCase saveClientUseCase;
    private final DeleteClientUseCase deleteClientUseCase;
    private final GetClientUseCase getClientUseCase;
    private final ListClientUseCase listClientUseCase;
    private final UpdateClientUseCase updateClientUseCase;

    public ClientController(SaveClientUseCase saveClientUseCase, DeleteClientUseCase deleteClientUseCase, GetClientUseCase getClientUseCase, ListClientUseCase listClientUseCase, UpdateClientUseCase updateClientUseCase) {
        this.saveClientUseCase = saveClientUseCase;
        this.deleteClientUseCase = deleteClientUseCase;
        this.getClientUseCase = getClientUseCase;
        this.listClientUseCase = listClientUseCase;
        this.updateClientUseCase = updateClientUseCase;
    }

    @PostMapping
    public void createClient(@Valid @RequestBody ClientDto clientDto) {
        saveClientUseCase.execute(clientDto);
    }

    @GetMapping
    public void listClients() {
        listClientUseCase.execute();
    }

    @GetMapping("/{id}")
    public void getClient(@PathVariable UUID id) {
        getClientUseCase.execute(id);
    }

    @PutMapping("/{id}")
    public void updateClient(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateClientRequest request
    ) {
        updateClientUseCase.execute(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteClient(@PathVariable UUID id) {
        deleteClientUseCase.execute(id);
    }
}
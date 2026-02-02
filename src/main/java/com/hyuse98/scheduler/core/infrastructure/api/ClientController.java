package com.hyuse98.scheduler.core.infrastructure.api;

import com.hyuse98.scheduler.core.application.dto.ClientDto;
import com.hyuse98.scheduler.core.application.usecases.client.DeleteClientUseCase;
import com.hyuse98.scheduler.core.application.usecases.client.GetClientUseCase;
import com.hyuse98.scheduler.core.application.usecases.client.ListClientUseCase;
import com.hyuse98.scheduler.core.application.usecases.client.SaveClientUseCase;
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

    public ClientController(SaveClientUseCase saveClientUseCase, DeleteClientUseCase deleteClientUseCase, GetClientUseCase getClientUseCase, ListClientUseCase listClientUseCase) {
        this.saveClientUseCase = saveClientUseCase;
        this.deleteClientUseCase = deleteClientUseCase;
        this.getClientUseCase = getClientUseCase;
        this.listClientUseCase = listClientUseCase;
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
    public void updateClient(@PathVariable UUID id) {
        //Todo()
    }

    @DeleteMapping("/{id}")
    public void deleteClient(@PathVariable UUID id) {
        deleteClientUseCase.execute(id);
    }
}
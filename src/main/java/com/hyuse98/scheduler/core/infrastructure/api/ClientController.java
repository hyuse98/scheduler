package com.hyuse98.scheduler.core.infrastructure.api;

import com.hyuse98.scheduler.core.application.dto.ClientDto;
import com.hyuse98.scheduler.core.application.usecases.client.SaveClientUseCase;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/client")
public class ClientController {

    private final SaveClientUseCase saveClientUseCase;

    public ClientController(SaveClientUseCase saveClientUseCase) {
        this.saveClientUseCase = saveClientUseCase;
    }

    @GetMapping
    public void listClients(){
        //Todo()
    }

    @GetMapping("/{id}")
    public void getClient(@PathVariable UUID id){
        //Todo()
    }

    @PostMapping
    public void createClient(@Valid @RequestBody ClientDto clientDto) {
        //Todo()
        saveClientUseCase.execute(clientDto);
    }

    @PutMapping("/{id}")
    public void updateClient(@PathVariable UUID id){
        //Todo()
    }

    @DeleteMapping("/{id}")
    public void deleteClient(@PathVariable UUID id){
        //Todo()
    }
}
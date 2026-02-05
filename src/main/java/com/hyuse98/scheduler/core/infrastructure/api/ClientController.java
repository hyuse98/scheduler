package com.hyuse98.scheduler.core.infrastructure.api;

import com.hyuse98.scheduler.core.application.dto.ClientResponse;
import com.hyuse98.scheduler.core.application.dto.UpdateClientRequest;
import com.hyuse98.scheduler.core.application.usecases.client.GetClientUseCase;
import com.hyuse98.scheduler.core.application.usecases.client.ListClientUseCase;
import com.hyuse98.scheduler.core.application.usecases.client.UpdateClientUseCase;
import com.hyuse98.scheduler.core.infrastructure.persistance.jpa.mapper.ClientEntityMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/client")
public class ClientController {
    private final GetClientUseCase getClientUseCase;
    private final ListClientUseCase listClientUseCase;
    private final UpdateClientUseCase updateClientUseCase;
    private final ClientEntityMapper clientEntityMapper;

    public ClientController(GetClientUseCase getClientUseCase, ListClientUseCase listClientUseCase, UpdateClientUseCase updateClientUseCase, ClientEntityMapper clientEntityMapper) {
        this.getClientUseCase = getClientUseCase;
        this.listClientUseCase = listClientUseCase;
        this.updateClientUseCase = updateClientUseCase;
        this.clientEntityMapper = clientEntityMapper;
    }

    @GetMapping
    public ResponseEntity<List<ClientResponse>> listClients() {
        var responseList = listClientUseCase.execute();
        return ResponseEntity.status(HttpStatus.OK).body(responseList.stream().map(clientEntityMapper::toResponse).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponse> getClient(@PathVariable UUID id) {
        var response = getClientUseCase.execute(id);
        return ResponseEntity.status(HttpStatus.OK).body(clientEntityMapper.toResponse(response));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ClientResponse> updateClient(@PathVariable UUID id, @Valid @RequestBody UpdateClientRequest request) {
        var response = updateClientUseCase.execute(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(clientEntityMapper.toResponse(response));
    }
}
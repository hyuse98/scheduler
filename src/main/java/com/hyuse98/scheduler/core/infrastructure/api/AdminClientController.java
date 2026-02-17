package com.hyuse98.scheduler.core.infrastructure.api;

import com.hyuse98.scheduler.core.application.dto.ClientResponse;
import com.hyuse98.scheduler.core.application.usecases.client.GetClientUseCase;
import com.hyuse98.scheduler.core.application.usecases.client.ListClientUseCase;
import com.hyuse98.scheduler.core.infrastructure.persistance.jpa.mapper.ClientEntityMapper;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/v1/admin/client")
public class AdminClientController {

    private final ListClientUseCase listClientUseCase;
    private final GetClientUseCase getClientUseCase;
    private final ClientEntityMapper clientEntityMapper;

    public AdminClientController(
            ListClientUseCase listClientUseCase,
            GetClientUseCase getClientUseCase,
            ClientEntityMapper clientEntityMapper) {
        this.listClientUseCase = listClientUseCase;
        this.getClientUseCase = getClientUseCase;
        this.clientEntityMapper = clientEntityMapper;
    }

    @GetMapping
    public ResponseEntity<List<ClientResponse>> listClients() {
        var responseList = listClientUseCase.execute();
        return ResponseEntity.status(HttpStatus.OK).body(
                responseList.stream()
                        .map(clientEntityMapper::toResponse)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponse> getClientById(@PathVariable UUID id) {
        var client = getClientUseCase.execute(id);
        return ResponseEntity.ok(clientEntityMapper.toResponse(client));
    }
}
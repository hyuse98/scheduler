package com.hyuse98.scheduler.core.infrastructure.api;

import com.hyuse98.scheduler.core.application.dto.CreateServiceProviderRequest;
import com.hyuse98.scheduler.core.application.dto.ServiceProviderResponse;
import com.hyuse98.scheduler.core.application.usecases.serviceprovider.CreateServiceProviderUseCase;
import com.hyuse98.scheduler.core.application.usecases.serviceprovider.GetServiceProviderUseCase;
import com.hyuse98.scheduler.core.infrastructure.persistance.jpa.mapper.ServiceProviderEntityMapper;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/v1/admin/service-provider")
public class AdminServiceProviderController {

    private final CreateServiceProviderUseCase createServiceProviderUseCase;
    private final GetServiceProviderUseCase getServiceProviderUseCase;
    private final ServiceProviderEntityMapper mapper;

    public AdminServiceProviderController(
            CreateServiceProviderUseCase createServiceProviderUseCase,
            GetServiceProviderUseCase getServiceProviderUseCase,
            ServiceProviderEntityMapper mapper) {
        this.createServiceProviderUseCase = createServiceProviderUseCase;
        this.getServiceProviderUseCase = getServiceProviderUseCase;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<ServiceProviderResponse> create(@RequestBody @Valid CreateServiceProviderRequest request) {
        // Apenas ADMINS acessam este endpoint para criar novos prestadores
        var savedProvider = createServiceProviderUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(savedProvider));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceProviderResponse> getById(@PathVariable UUID id) {
        var provider = getServiceProviderUseCase.execute(id);
        return ResponseEntity.ok(mapper.toResponse(provider));
    }

    //TODO(Enable and Disable Endpoints)
}
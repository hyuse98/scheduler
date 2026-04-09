package com.hyuse98.scheduler.core.infrastructure.api;

import com.hyuse98.scheduler.core.application.dto.ServiceProviderResponse;
import com.hyuse98.scheduler.core.application.usecases.serviceprovider.GetServiceProviderUseCase;
import com.hyuse98.scheduler.core.infrastructure.persistance.jpa.mapper.ServiceProviderEntityMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Admin - Service Provider", description = "Endpoints administrativos para prestadores de serviços")
@PreAuthorize("hasRole('ADMIN')")
@RestController
@RequestMapping("/api/v1/admin/service-provider")
public class AdminServiceProviderController {

    private final GetServiceProviderUseCase getServiceProviderUseCase;
    private final ServiceProviderEntityMapper mapper;

    public AdminServiceProviderController(
            GetServiceProviderUseCase getServiceProviderUseCase,
            ServiceProviderEntityMapper mapper) {
        this.getServiceProviderUseCase = getServiceProviderUseCase;
        this.mapper = mapper;
    }

    @Operation(summary = "Obter prestador por ID", description = "Busca os detalhes de um prestador específico")
    @GetMapping("/{id}")
    public ResponseEntity<ServiceProviderResponse> getById(@PathVariable UUID id) {
        var provider = getServiceProviderUseCase.execute(id);
        return ResponseEntity.ok(mapper.toResponse(provider));
    }

    //TODO(Enable and Disable Endpoints)
}
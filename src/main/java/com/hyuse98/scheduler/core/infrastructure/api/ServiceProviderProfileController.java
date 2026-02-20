package com.hyuse98.scheduler.core.infrastructure.api;

import com.hyuse98.scheduler.core.application.dto.ServiceProviderResponse;
import com.hyuse98.scheduler.core.application.dto.UpdateServiceProviderRequest;
import com.hyuse98.scheduler.core.application.usecases.serviceprovider.GetServiceProviderUseCase;
import com.hyuse98.scheduler.core.application.usecases.serviceprovider.UpdateServiceProviderUseCase;
import com.hyuse98.scheduler.core.infrastructure.persistance.jpa.mapper.ServiceProviderEntityMapper;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@PreAuthorize("hasRole('USER')")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/v1/provider/me")
public class ServiceProviderProfileController {

    private final GetServiceProviderUseCase getServiceProviderUseCase;
    private final UpdateServiceProviderUseCase updateServiceProviderUseCase;
    private final ServiceProviderEntityMapper mapper;

    public ServiceProviderProfileController(
            GetServiceProviderUseCase getServiceProviderUseCase,
            UpdateServiceProviderUseCase updateServiceProviderUseCase,
            ServiceProviderEntityMapper mapper) {
        this.getServiceProviderUseCase = getServiceProviderUseCase;
        this.updateServiceProviderUseCase = updateServiceProviderUseCase;
        this.mapper = mapper;
    }

    @GetMapping
    public ResponseEntity<ServiceProviderResponse> getMyProfile(Principal principal) {
        String loggedInEmail = principal.getName();
        var provider = getServiceProviderUseCase.execute(loggedInEmail);
        return ResponseEntity.ok(mapper.toResponse(provider));
    }

    @PatchMapping
    public ResponseEntity<ServiceProviderResponse> updateMyProfile(Principal principal, @Valid @RequestBody UpdateServiceProviderRequest request) {
        String loggedInEmail = principal.getName();
        var loggedInProvider = getServiceProviderUseCase.execute(loggedInEmail);

        var updatedProvider = updateServiceProviderUseCase.execute(loggedInProvider.getId(), request);
        return ResponseEntity.ok(mapper.toResponse(updatedProvider));
    }
}
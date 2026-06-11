package com.hyuse98.scheduler.core.application.usecases.client;

import com.hyuse98.scheduler.core.application.dto.CreateClientRequest;
import org.springframework.stereotype.Service;

@Service
public interface SaveClientUseCase {
    void execute(CreateClientRequest createClientRequest);
}
package com.hyuse98.scheduler.core.application.usecases.client;

import com.hyuse98.scheduler.core.application.dto.ClientDto;
import org.springframework.stereotype.Service;

@Service
public interface SaveClientUseCase {
    void execute(ClientDto clientDto);
}
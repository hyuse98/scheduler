package com.hyuse98.scheduler.core.application.usecases.schedule.impl;

import com.hyuse98.scheduler.core.application.dto.CreateScheduleRequest;
import com.hyuse98.scheduler.core.application.exceptions.ClientNotFoundException;
import com.hyuse98.scheduler.core.application.exceptions.ServiceProviderNotFoundException;
import com.hyuse98.scheduler.core.application.usecases.schedule.CreateScheduleUseCase;
import com.hyuse98.scheduler.core.domain.model.Client;
import com.hyuse98.scheduler.core.domain.model.Schedule;
import com.hyuse98.scheduler.core.domain.repository.ClientRepository;
import com.hyuse98.scheduler.core.domain.repository.ScheduleRepository;
import com.hyuse98.scheduler.core.domain.repository.ServiceProviderRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CreateScheduleUseCaseImpl implements CreateScheduleUseCase {

    private final ScheduleRepository scheduleRepository;
    private final ClientRepository clientRepository;
    private final ServiceProviderRepository serviceProviderRepository;

    public CreateScheduleUseCaseImpl(
            ScheduleRepository scheduleRepository,
            ClientRepository clientRepository,
            ServiceProviderRepository serviceProviderRepository) {
        this.scheduleRepository = scheduleRepository;
        this.clientRepository = clientRepository;
        this.serviceProviderRepository = serviceProviderRepository;
    }

    @Transactional
    @Override
    public Schedule execute(String clientEmail, CreateScheduleRequest request) {
        // 1. Garante que o cliente que está a fazer o pedido existe
        Client client = clientRepository.findByEmail(clientEmail)
                .orElseThrow(() -> new ClientNotFoundException("Cliente não encontrado com o email fornecido."));

        // 2. Garante que o prestador de serviço escolhido existe
        if (!serviceProviderRepository.existsById(request.serviceProviderId())) {
            throw new ServiceProviderNotFoundException("O prestador de serviço selecionado não existe.");
        }

        // 3. Cria o agendamento associando o ID do cliente logado (segurança para não agendar por outros)
        Schedule newSchedule = Schedule.create(
                UUID.randomUUID(),
                client.getId(),
                request.serviceProviderId(),
                request.serviceType(),
                request.description(),
                request.scheduledAt()
        );

        newSchedule.validate();

        return scheduleRepository.save(newSchedule);
    }
}
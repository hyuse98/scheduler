package com.hyuse98.scheduler.core.application.usecases.client;

import com.hyuse98.scheduler.core.domain.model.Client;

import java.util.List;

public interface ListClientUseCase {

    List<Client> execute();
}

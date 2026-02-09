package com.hyuse98.scheduler.iam.application.usecase;

import com.hyuse98.scheduler.iam.domain.model.aggregate.User;

import java.util.List;

public interface GetUsersUsecase {

    List<User> execute();

}

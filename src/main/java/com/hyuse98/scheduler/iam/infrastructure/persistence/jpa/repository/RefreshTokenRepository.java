package com.hyuse98.scheduler.iam.infrastructure.persistence.jpa.repository;

import com.hyuse98.scheduler.iam.infrastructure.persistence.jpa.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
}

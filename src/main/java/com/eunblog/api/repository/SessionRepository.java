package com.eunblog.api.repository;

import com.eunblog.api.domain.Session;
import org.springframework.data.repository.CrudRepository;

public interface SessionRepository extends CrudRepository<Session,Long> {
}

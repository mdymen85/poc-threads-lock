package com.lock.nowait;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.hibernate.LockOptions;
import org.hibernate.cfg.AvailableSettings;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TestEntityRepository extends CrudRepository<MainEntity, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({@QueryHint(name = AvailableSettings.JPA_LOCK_TIMEOUT, value = LockOptions.SKIP_LOCKED + "")})
    List<MainEntity> findByGenero(String genero, PageRequest pageRequest);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<MainEntity> findByGeneroAndNome(String masculino, String nome0XXX);
}

package com.lock.nowait;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SecondaryRepository extends CrudRepository<SecondaryEntity, Long> {

    Optional<SecondaryEntity> findByProcessed(Long processed);

}

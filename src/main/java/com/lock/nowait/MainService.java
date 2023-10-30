package com.lock.nowait;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.PageRequest;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class MainService {

    private final TestEntityRepository testEntityRepository;
    private final SecondaryRepository repository;

    @OnRetryable
    public void doRetry() {
        try {
            log.info("Thread = {}", Thread.currentThread().getName());
            Optional<MainEntity> opt = this.testEntityRepository.findByGeneroAndNome("MASCULINO", "NOME_0XXX");
            opt.ifPresent(entity -> log.info("main entity = {}", entity));
            opt.ifPresent(this.testEntityRepository::save);
            throw new Exception();
        }
        catch (Exception e) {
            log.error("Error in retry method ");
            throw new RuntimeException();
        }


    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateName(MainEntity classX) {
        classX.setNome("Pepito");
        this.testEntityRepository.save(classX);
    }

    /**
     * In a new transaction, will count how many times will
     * pass throw a specific row, and will save in a secundary table.
     *
     * @param id from the mainEntity
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateSecondaryTable(Long id) {
        Optional<SecondaryEntity> optSec = this.repository.findByProcessed(id);

        var sec = optSec.orElseGet(() -> SecondaryEntity.builder()
                .processed(id)
                .times(1L)
                .build());

        this.repository.save(sec);
    }

    public List<MainEntity> findAllMasculino() {
        return this.testEntityRepository.findByGenero("MASCULINO", PageRequest.of(0, 100) );
    }

}

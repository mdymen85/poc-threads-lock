package com.lock.nowait.jobs;

import com.lock.nowait.MainEntity;
import com.lock.nowait.TestEntityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

@Component
@Slf4j
@RequiredArgsConstructor
public class TaskExecutorJob {

    @Autowired
    @Qualifier("customThreadPoolExecutor")
    private TaskExecutor executor;

    private final TestEntityRepository testEntityRepository;

    @Transactional
//    @Scheduled(cron = "${application.cron:*/10 * * * * *}")
    public void job() throws InterruptedException {

        log.info("Job is starting...");

        List<MainEntity> entities = testEntityRepository.findByGenero("MASCULINO", PageRequest.of(0, 100));
        CountDownLatch latch = new CountDownLatch(entities.size());

        Random random = new Random();
        entities.forEach((entity) -> {
            executor.execute(() -> {
                int randomNumber = random.nextInt(501) + 500;
                try {
                    Thread.sleep(randomNumber);
                    log.info(Thread.currentThread().getName() + " " + entity.getGenero() + " " + entity.getNome());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    latch.countDown();
                }
            });
        });

        latch.await();

        log.info("Job is ended...");

    }

}

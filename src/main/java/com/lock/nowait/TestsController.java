package com.lock.nowait;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.retry.annotation.Retryable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.Executor;

@RestController
@RequiredArgsConstructor
//@OnConditionOne
//@OnConditionTwo
@Slf4j
public class TestsController {

    @Autowired
    @Qualifier("customThreadPoolExecutor")
    private Executor executor;

    private final TestEntityRepository testEntityRepository;
    private final MainService mainService;

    @PostConstruct
    public void post() {
        log.info("----> SUBIU !!");
    }

    @RequestMapping(path = "/retry", method = RequestMethod.POST)
    @Transactional
    public void retry() {
        this.mainService.doRetry();
    }

    @RequestMapping(path = "/transaction", method = RequestMethod.POST)
    @Transactional
    public void transaction() throws Exception {

        try {
           var t = MainEntity.builder()
                    .genero("ET")
                    .nome("Julio")
                    .build();

           testEntityRepository.save(t);
           throw new Exception();
        }
        catch (Exception e) {
            var t = MainEntity.builder()
                    .genero("MASSAPAM")
                    .nome("Trindade")
                    .build();

            testEntityRepository.save(t);
        }

    }


    @RequestMapping(path = "/add", method = RequestMethod.POST)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void add() {
        for (int i = 0; i < 1000; i++) {
            MainEntity t = null;
            if (i % 2 == 0) {
                t = MainEntity.builder()
                        .genero("MASCULINO")
                        .nome("NOME_" + i)
                        .build();
            } else {
                t = MainEntity.builder()
                        .genero("FEMENINO")
                        .nome("NOME_" + i)
                        .build();
            }

            testEntityRepository.save(t);
        }
    }

    /**
     * Will lock the rows loaded from the database and will
     * update a row in a new transaction (REQUIRES_NEW) in a new table.
     *
     * This will lead in a successful cenario, because the table
     * updated is not locked
     *
     * @throws InterruptedException
     */
    @RequestMapping(path = "/auxiliartable", method = RequestMethod.GET)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateAuxiliarTableInNewTransaction() throws InterruptedException {
        List<MainEntity> mainEntityList = this.mainService.findAllMasculino();
        mainEntityList.forEach(c -> {
            this.mainService.updateSecondaryTable(c.getId());
        });

        Thread.sleep(10000);
    }
    @RequestMapping(path = "/skiplocked", method = RequestMethod.GET)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void get3() throws InterruptedException {
        List<MainEntity> fx = this.testEntityRepository.findByGenero("MASCULINO", PageRequest.of(0, 100) );

        for (MainEntity f: fx) {
            log.info(f.toString());
        }
        Thread.sleep(100000);

    }
    @RequestMapping(path = "/get1", method = RequestMethod.GET)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void get1() throws InterruptedException {
        List<MainEntity> fx = this.testEntityRepository.findByGenero("MASCULINO", PageRequest.of(0, 100) );

        for (MainEntity f: fx) {
            try {
                log.info(f.toString());
                throw new RuntimeException();
            }
            catch (Exception e) {
                log.error("Exception " + f.toString());
                this.mainService.updateSecondaryTable(f.getId());
            }
        //    this.classXService.updateName(f);
        }

        log.info("TERMINOU !");


//        Thread.sleep(100000);

    }

    @RequestMapping(path = "/get2", method = RequestMethod.GET)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void get2() {
        List<MainEntity> fx = this.testEntityRepository.findByGenero("MASCULINO", PageRequest.of(0, 100) );

        mainService.updateName(fx.get(0));

    }

    @RequestMapping(path = "/test", method = RequestMethod.POST)
    public void test() {

        Random random = new Random();

        for (int i = 0; i < 1000; i++) {



            int finalI = i;
            executor.execute(() -> {
                int randomNumber = random.nextInt(501) + 500;
                try {
                    Thread.sleep(randomNumber);
                    System.out.println(Thread.currentThread().getName() + " " + finalI);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });

        }
    }

}

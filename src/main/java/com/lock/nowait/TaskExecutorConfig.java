package com.lock.nowait;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class TaskExecutorConfig {


    @Bean(name = "customThreadPoolExecutor")
    public TaskExecutor customThreadPoolExecutor() {
        int corePoolSize = 3;
        int maximumPoolSize = 3;
        long keepAliveTime = 10L;

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);  // Número de threads iniciais.
        executor.setMaxPoolSize(maximumPoolSize);  // Máximo de threads.
        executor.setQueueCapacity(300);  // Fila de tarefas esperando por uma thread.
        executor.setThreadNamePrefix("thread");
        executor.initialize();
        return executor;
    }

}

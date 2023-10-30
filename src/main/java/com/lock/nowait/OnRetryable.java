package com.lock.nowait;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retryable(value = { Exception.class},
        maxAttemptsExpression = "${application.retry-409-pending.attempts:10}",
        backoff = @Backoff(delayExpression = "${application.retry-409-pending.backoff:1000}"))
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD, ElementType.ANNOTATION_TYPE })
public @interface OnRetryable {
}

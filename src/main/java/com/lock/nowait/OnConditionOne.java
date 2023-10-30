package com.lock.nowait;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@ConditionalOnProperty(name = "application.condition1.enabled", havingValue = "true", matchIfMissing = false)
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface OnConditionOne {
}
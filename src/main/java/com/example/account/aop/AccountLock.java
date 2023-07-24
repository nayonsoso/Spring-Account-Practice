package com.example.account.aop;

import java.lang.annotation.*;

@Target(ElementType.METHOD) // 어노테이션을 붙을 수 있는 타겟
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface AccountLock { // 어노테이션 인터페이스
    long tryLockTime() default 5000L; // 5초동안 잡고 있겠다.
}
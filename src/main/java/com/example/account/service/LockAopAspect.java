package com.example.account.service;

import com.example.account.aop.AccountLockIdInterface;
import com.example.account.dto.CancelBalance;
import com.example.account.dto.UseBalance;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect // 에스팩트로 스트링 빈 등록
@Component
@Slf4j
@RequiredArgsConstructor
public class LockAopAspect {
    private final LockService lockService;

    // 이건 aspectj의 문법임 - 아래 대상에 이런 것을 적용하라는 내용!
    @Around("@annotation(com.example.account.aop.AccountLock) && args(request)")
    public Object aroundMethod(
            ProceedingJoinPoint pjp,
            AccountLockIdInterface request
    ) throws Throwable {
        // lock 취득 시도
        lockService.lock(request.getAccountNumber()); // 핵심 동작 전에 락
        try {
            return pjp.proceed(); // 핵심 관심사를 실행시키는 부분
        } finally {
            // lock 해제
            lockService.unlock(request.getAccountNumber()); // 핵심 동작 후에 언락
        }
    }
}
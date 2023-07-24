package com.example.account.service;

import com.example.account.domain.Account;
import com.example.account.exception.AccountException;
import com.example.account.type.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class LockService {
    private final RedissonClient redissonClient;

    public void lock(String accountNumber) {
        // 계좌 번호를 lock의 키로 사용하는 락을 반환
        RLock lock = redissonClient.getLock(getLockKey(accountNumber));
        log.debug("Trying lock for accountNumber : {}", accountNumber);

        try {
            // 첫번째 인자인 waitTime은 락을 기다리는 시간
            // 두번째 인자인 leaseTime은 자동으로 락을 반환하는 시간
            boolean isLock = lock.tryLock(1, 15, TimeUnit.SECONDS);
            if(!isLock) {
                log.error("======Lock acquisition failed=====");
                // 계좌가 사용중이라는 에러를 던짐
                throw new AccountException(ErrorCode.ACCOUNT_TRANSACTION_LOCK);
            }
        } catch (AccountException e){ // 락이 중복되는 경우 여기서 잡아줌
            throw e;
        } catch (Exception e) {
            log.error("Redis lock failed", e);
        }
    }

    public void unlock(String accountNumber) {
        log.debug("Unlock for accountNumber : {} ", accountNumber);
        redissonClient.getLock(getLockKey(accountNumber)).unlock(); // 락키로 락을 가져온 다음 락을 풀어줌
    }

    // 락 키의 형식을 만들어주는 함수
    private String getLockKey(String accountNumber) {
        return "ACLK:" + accountNumber;
    }
}
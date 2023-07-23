package com.example.account.repository;

import com.example.account.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findFirstByOrderByIdDesc(); // 가장 id(계좌번호)가 큰 값을 리턴
    // 이름을 형식에 맞게 생성해준 것만 있는데, 알아서 쿼리를 짜주는 편리한 기능이다.
    // (jpaRepository에서 제공하는 기능)
}

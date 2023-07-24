package com.example.account.repository;

import com.example.account.domain.Account;
import com.example.account.domain.AccountUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findFirstByOrderByIdDesc(); // 가장 id(계좌번호)가 큰 값을 리턴
    // 이름을 형식에 맞게 생성해준 것만 있는데, 알아서 쿼리를 짜주는 편리한 기능이다.
    // (jpaRepository에서 제공하는 기능)

    // 아래 코드가 가능한 이유 : Account 도메인 안에
    // @ManyToOne 으로 AccountUser accountUser; 인스턴스를 선언했기 때문에
    // 자동으로 두 도메인(테이블)이 연결된 것이다 - 아마 JPA의 문법인 것 같은데..!
    Integer countByAccountUser(AccountUser accountUser);

    Optional<Account> findByAccountNumber(String AccountNumber);

    List<Account> findByAccountUser(AccountUser accountUser);
}

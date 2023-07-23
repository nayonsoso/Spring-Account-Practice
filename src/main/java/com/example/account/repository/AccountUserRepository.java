package com.example.account.repository;

import com.example.account.domain.AccountUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountUserRepository extends JpaRepository<AccountUser, Long> {
    // <,> 안의 첫번째 파라미터는 엔티티 타입, 두번째 파라미터는 PK의 타입
}

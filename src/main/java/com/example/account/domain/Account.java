package com.example.account.domain;

import com.example.account.exception.AccountException;
import com.example.account.type.AccountStatus;
import com.example.account.type.ErrorCode;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
/*
@EntityListeners(AuditingEntityListener.class) // @createdDated와 @LastModified를 유효하게 하기 위한 어노테이션
베이스 엔티티로 묶어줌
*/
public class Account extends BaseEntity{
    // 이름을 user로 하면 디비가 가지고 있는 user 테이블과 헷갈릴 수 있으므로 AccountUser로 설정
    @ManyToOne // 계좌 N개가 유저 1명과 대응되므로
    private AccountUser accountUser;

    private String accountNumber;

    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;

    private Long balance;

    /*
    * 아래처럼 했었는데 리팩터링해서 베이스 엔티티로 묶어줌
    // 자동으로 시간을 저장해주는 어노테이션을 이용
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;
     */

    private LocalDateTime registeredAt;
    private LocalDateTime unRegisteredAt;

    public void useBalance(Long amount){
        if(amount > balance){
            throw new AccountException(ErrorCode.AMOUNT_EXCEED_BALANCE);
        }
        balance -= amount;
    }
    public void cancelBalance(Long amount) {
        if (amount < 0) {
            throw new AccountException(ErrorCode.INVALID_REQUEST);
        }
        balance += amount; // 취소된 양만큼 복구해주기
    }
}
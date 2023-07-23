package com.example.account.domain;

import com.example.account.type.AccountStatus;
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
@EntityListeners(AuditingEntityListener.class) // @createdDated와 @LastModified를 유효하게 하기 위한 어노테이션
public class Account {
    @Id
    @GeneratedValue
    private Long id;

    // 이름을 user로 하면 디비가 가지고 있는 user 테이블과 헷갈릴 수 있으므로 AccountUser로 설정
    @ManyToOne // 계좌 N개가 유저 1명과 대응되므로
    private AccountUser accountUser;

    private String accountNumber;

    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;

    private Long balance;

    private LocalDateTime registeredAt;
    private LocalDateTime unregisteredAt;

    // 자동으로 시간을 저장해주는 어노테이션을 이용
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;
}

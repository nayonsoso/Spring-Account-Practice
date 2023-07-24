package com.example.account.domain;

import com.example.account.type.AccountStatus;
import com.example.account.type.TransactionResultType;
import com.example.account.type.TransactionType;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Transaction extends BaseEntity { // BaseEntity에 Listenner가 있으므로 자동으로 생성, 수정 시간 만듦
    @Enumerated(EnumType.STRING) // ENUM을 string으로 저장하기 위해
    private TransactionType transactionType;
    @Enumerated(EnumType.STRING)
    private TransactionResultType transactionResultType;

    @ManyToOne
    private Account account; // 계좌 하나 당 여러 거래 연결
    private Long amount;
    private Long balanceSnapshot;

    private String transactionId; // pk가 아닌 별도의 거래 아이디 생성
    private LocalDateTime transactedAt;
}
package com.example.account.dto;

import com.example.account.domain.Account;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountDto {
    private Long userId;
    private String accountNumber;
    private Long balance;

    private LocalDateTime registeredAt;
    private LocalDateTime ubregisteredAt;

    // Account를 AccountDto로 바꾸는 함수
    // 이런 dto는 엔티티를 통해서 만들어지는 경우가 많으므로 이런 함수를 static으로 만들어주면
    // 좋다!
    public static AccountDto fromEntity(Account account){
        return AccountDto.builder()
                .userId(account.getAccountUser().getId())
                .accountNumber(account.getAccountNumber())
                .registeredAt(account.getRegisteredAt())
                .ubregisteredAt(account.getUnregisteredAt())
                .build();
    }
}

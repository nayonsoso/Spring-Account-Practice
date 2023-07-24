package com.example.account.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountInfo { // Accoun가 있는데 왜 만듦?
    // AccountDto는 컨트롤러와 서비스를 이어주고, AccountInfo는 클라이언트와 서비스를 이어주는 것으로 만들기 위함
    // 이렇게 전용 DTO를 만들지 않으면 오류가 생길 수 있기 때문에 이렇게 특정 목적이 있는 dto를 만드는 것임
    private String accountNumber;
    private Long balance;
}
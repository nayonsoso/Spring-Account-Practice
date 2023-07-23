package com.example.account.exception;

import com.example.account.type.ErrorCode;
import lombok.*;

// 거의 기본적으로 커스텀 익셉션은 런타임 익셉션을 기반으로 한다.
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountException extends RuntimeException {
    private ErrorCode errorCode;
    private String errorMessage;

    public AccountException(ErrorCode errorCode){
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDescription();
    }
}
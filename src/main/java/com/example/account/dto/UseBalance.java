package com.example.account.dto;

import com.example.account.aop.AccountLockIdInterface;
import com.example.account.type.TransactionResultType;
import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

public class UseBalance {
    @Getter
    @Setter
    @AllArgsConstructor
    public static class Request implements AccountLockIdInterface {
        @NotNull
        @Min(1)
        private Long userId;

        @NotBlank
        @Size(min = 10, max = 10) // 딱 10자리
        private String accountNumber;

        @NotNull
        @Min(10)
        @Max(1000_000_000) // 이렇게 세자리씩 끊을 수도 있음, 최대 거래금액(한도)를 넣어줘야 함
        private Long amount;
    }

    /**
     * {
     *    "accountNumber":"1234567890",
     * 	 "transactionResult":"S",
     * 	 "transactionId":"c2033bb6d82a4250aecf8e27c49b63f6",
     * 	 "amount":1000,
     *    "transactedAt":"2022-06-01T23:26:14.671859"
     * }
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private String accountNumber;
        private TransactionResultType transactionResult;
        private String transactionId;
        private Long amount;
        private LocalDateTime transactedAt; // 거리 시간

        public static Response from(TransactionDto transactionDto) { // TransactionDto를 Response로!
            return Response.builder()
                    .accountNumber(transactionDto.getAccountNumber())
                    .transactionResult(transactionDto.getTransactionResultType())
                    .transactionId(transactionDto.getTransactionId())
                    .amount(transactionDto.getAmount())
                    .transactedAt(transactionDto.getTransactedAt())
                    .build();
        }
    }
}
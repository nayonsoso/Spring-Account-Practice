package com.example.account.dto;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

// 하나의 createAccount라는 클래스 안에서 staitc class인 Request와 Response를 만들면 더 이름을 명시하기 쉬움!
// -> 강사님이 선호하시는 방법
public class CreateAccount {
    @Getter
    @Setter
    @AllArgsConstructor
    public static class Request{
        // 컨트롤러에서 @Valid를 request앞에 달아주었는데, 이 @Valid가 제대로 작동하기 위해서는
        // 어떤것이 @Valid 조건인지도 명시를 해줘야 함 -> @NotNull, @Min
        @NotNull
        @Min(1)
        private Long userId;

        @NotNull
        @Min(100)
        private Long initialBalance;
    }

    @Getter
    @Setter
    @NoArgsConstructor //여기서부터 세개의 어노테이션은 왜 사용하는지 잘 모르겠음
    @AllArgsConstructor
    @Builder
    public static class Response{
        private Long userId;
        private String accountNumber;
        private LocalDateTime registeredAt;

        public static Response from(AccountDto accountDto){
            return Response.builder()
                    .userId(accountDto.getUserId())
                    .accountNumber(accountDto.getAccountNumber())
                    .registeredAt(accountDto.getRegisteredAt())
                    .build();
        }
    }
}
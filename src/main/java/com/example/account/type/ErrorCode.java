package com.example.account.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor // 이 어노테이션을 해줌으로써 description을 인자로 갖는 생성자를 만들어주므로
public enum ErrorCode {
    USER_NOT_FOUND("사용자가 없습니다"); // 이런 문법이 가능한 것임

    private final String description;
}
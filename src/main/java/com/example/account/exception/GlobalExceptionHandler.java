package com.example.account.exception;

import com.example.account.dto.ErrorResponse;
import com.example.account.type.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.example.account.type.ErrorCode.INTERNAL_SERVER_ERROR;
import static com.example.account.type.ErrorCode.INVALID_REQUEST;


@Slf4j
@RestControllerAdvice // 모든 에러를 잡아줄 클로벌 에러 핸들러라는 뜻
public class GlobalExceptionHandler {

    @ExceptionHandler(AccountException.class) // 계좌에 관련된 에러를 핸들링 한다는 뜻
    public ErrorResponse handleAccountException(AccountException e) {
        log.error("{} is occurred.", e.getErrorCode());

        return new ErrorResponse(e.getErrorCode(), e.getErrorMessage());
    }

    // 함수에 잘못된 값이 들어오는 경우 e.g. @Valid가 지켜지지 않았을 때
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException is occurred.", e);

        return new ErrorResponse(INVALID_REQUEST, INVALID_REQUEST.getDescription());
    }

    // IntegrityViolation 즉, 키가 중복되는 경우
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ErrorResponse handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        log.error("DataIntegrityViolationException is occurred.", e);

        return new ErrorResponse(INVALID_REQUEST, INVALID_REQUEST.getDescription());
    }

    // 앞에서 잡지 못한 모든 에러를 잡아줌
    @ExceptionHandler(Exception.class)
    public ErrorResponse handleException(Exception e) {
        log.error("Exception is occurred.", e);

        return new ErrorResponse(
                INTERNAL_SERVER_ERROR,
                INTERNAL_SERVER_ERROR.getDescription()
        );
    }
}
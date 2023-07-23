package com.example.account.controller;

import com.example.account.domain.Account;
import com.example.account.dto.AccountDto;
import com.example.account.dto.CreateAccount;
import com.example.account.service.AccountService;
import com.example.account.service.RedisTestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;
    private final RedisTestService redisTestService;

    @PostMapping("/account")
    public CreateAccount.Response createAccount( //post 방식이므로 request body 사용
            @RequestBody @Valid CreateAccount.Request request
    ) {
        // 여기서 리턴되는 AccountDto는 서비스와 컨트롤러 소통용이기 때문에
        // 응답용 CreateAccount.Response로 바꿔줘야 함 - fromEntity함수를 Response안에 생성!
        // AccountDto accountDto = accountService.createAccount(request.getUserId(), request.getInitialBalance());

        return CreateAccount.Response.from(
                accountService.createAccount(
                        request.getUserId(),
                        request.getInitialBalance()));
    }

    @GetMapping("/get-lock")
    public String getLock() {
        return redisTestService.getLock();
    }

    @GetMapping("/account/{id}")
    public Account getAccount(
            @PathVariable Long id){
        return accountService.getAccount(id);
    }
}

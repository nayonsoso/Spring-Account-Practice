package com.example.account.controller;

import com.example.account.domain.Account;
import com.example.account.dto.AccountDto;
import com.example.account.dto.AccountInfo;
import com.example.account.dto.CreateAccount;
import com.example.account.dto.DeleteAccount;
import com.example.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

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

    @DeleteMapping("/account")
    public DeleteAccount.Response deleteAccount(
            @RequestBody @Valid DeleteAccount.Request request
    ) {
        return DeleteAccount.Response.from( //
                accountService.deleteAccount(
                        request.getUserId(),
                        request.getAccountNumber()
                )
        );
    }

    @GetMapping("/account")
    public List<AccountInfo> getAccountsByUserId(
            @RequestParam("user_id") Long userId // 왜 여기가 requestParam인 걸까..
    ) {
        return accountService.getAccountsByUserId(userId)
                .stream().map(accountDto -> // accountDto list를 AccountInfo로 바꾸는 부분
                        AccountInfo.builder()
                                .accountNumber(accountDto.getAccountNumber())
                                .balance(accountDto.getBalance())
                                .build())
                .collect(Collectors.toList());
    }
}

package com.example.account.service;

import com.example.account.domain.Account;
import com.example.account.domain.AccountUser;
import com.example.account.dto.AccountDto;
import com.example.account.repository.AccountUserRepository;
import com.example.account.type.AccountStatus;
import com.example.account.repository.AccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {
    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountUserRepository accountUserRepository;

    @InjectMocks // 위에서 목으로 만들어준 객체가 이 서비스에 담기게 됨
    private AccountService accountService;

    @Test
    void createAccountSuccess() {
        //given 왜 createAccount를 위해서 아래와 같은 모킹이 필요한지 모르겠음
        AccountUser user = AccountUser.builder()
                .id(12L)
                .name("Pobi").build();
        given(accountUserRepository.findById(anyLong()))
                .willReturn(Optional.of(user));
        given(accountRepository.findFirstByOrderByIdDesc())
                .willReturn(Optional.of(Account.builder()
                        .accountNumber("1000000012").build()));
        given(accountRepository.save(any()))
                .willReturn(Account.builder()
                        .accountUser(user)
                        .accountNumber("1000000015").build());

        // 강사님은 '가장 최근에 만든 id'에 1을 더한 것이 정녕 맞는지를 검증하기 위해
        // argument captor을 이용할 수 있다고 하셨는데 왜 그걸 이용하면 입증이 되는지 이해가 안감
        ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);


        //when
        AccountDto accountDto = accountService.createAccount(1L,1000L);
        //then
        verify(accountRepository, times(1)).save(captor.capture());
        assertEquals(12L, accountDto.getUserId());
        assertEquals("1000000013", captor.getValue().getAccountNumber());
    }
}
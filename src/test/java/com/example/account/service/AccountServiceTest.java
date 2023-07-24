package com.example.account.service;

import com.example.account.domain.Account;
import com.example.account.domain.AccountUser;
import com.example.account.dto.AccountDto;
import com.example.account.exception.AccountException;
import com.example.account.repository.AccountUserRepository;
import com.example.account.type.AccountStatus;
import com.example.account.repository.AccountRepository;
import com.example.account.type.ErrorCode;
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
        // ArgumentCaptor에 대해
        // .save(captor)는 accountRepository의 save에 전달된 argument(여기서는 포비, 계좌번호가 1000013인 경우)의 값을 담음
        // 그래서 아래 aeertEquals에서 13이면 true가 나오고, save가 리턴한 값을 담고있는 accountDto에 대해서는 15가 맞는 것임
        assertEquals(12L, accountDto.getUserId());
        assertEquals("1000000013", captor.getValue().getAccountNumber());
        // assertEquals("1000000015", accountDto.getAccountNumber()); 는 true가 나옴,
        // 왜 accountRepository.save까지 리턴하는 것을 정해줘서 save가 리턴하는 accountDto랑
        // 실제로 저장된 값을 captor하는 거랑 다르게 하는건진 모르겠음
    }

    @Test
    void createFirstAccount() {
        //given
        AccountUser user = AccountUser.builder()
                .name("Pobi").build();
        user.setId(15L);
        given(accountUserRepository.findById(anyLong()))
                .willReturn(Optional.of(user));
        given(accountRepository.findFirstByOrderByIdDesc())
                .willReturn(Optional.empty()); // 아무런 계좌도 생성이 되지 않았을 때의 상황을 설정
        // 아무런 계좌도 생성되지 않은 상태에서 계좌번호가 "1000000000"인 계좌를 생성하는 로직을 서비스에서 짰었음
        given(accountRepository.save(any()))
                .willReturn(Account.builder()
                        .accountUser(user)
                        .accountNumber("1000000015").build());
        ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);

        //when
        AccountDto accountDto = accountService.createAccount(1L, 1000L);

        //then
        verify(accountRepository, times(1)).save(captor.capture());
        assertEquals(15L, accountDto.getUserId());
        assertEquals("1000000000", captor.getValue().getAccountNumber());
    }

    @Test
    @DisplayName("해당 유저 없음 - 계좌 생성 실패")
    void createAccount_UserNotFound() {
        //given - 상황 설정 : 일치하는 아이디가 없어서 accountUserRepository.findById가 엠프티를 리턴
        given(accountUserRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        //when - 주어진 상황에서 craeteAccount를 하려는 경우 에러가 발생되는지
        AccountException exception = assertThrows(AccountException.class,
                () -> accountService.createAccount(1L, 1000L));

        //then - 결과로 나오는 것의 ErrorCode가 ErrorCode.USER_NOT_FOUND인지 검증
        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("유저 당 최대 계좌는 10개")
    void createAccount_maxAccountIs10() {
        //given
        AccountUser user = AccountUser.builder()
                .name("Pobi").build();
        user.setId(15L);
        given(accountUserRepository.findById(anyLong()))
                .willReturn(Optional.of(user));
        given(accountRepository.countByAccountUser(any()))
                .willReturn(10); // 계좌의 갯수를 조회했을 때 무조건 10을 리턴하게 설정

        //when - create했을 때 나오는 에러를 잡아줌
        AccountException exception = assertThrows(AccountException.class,
                () -> accountService.createAccount(1L, 1000L));

        //then - 그러면 아래와 같은 에러 코드가 나와야 함
        assertEquals(ErrorCode.MAX_ACCOUNT_PER_USER_10, exception.getErrorCode());
    }

}
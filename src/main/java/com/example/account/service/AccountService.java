package com.example.account.service;

import com.example.account.domain.Account;
import com.example.account.domain.AccountUser;
import com.example.account.dto.AccountDto;
import com.example.account.exception.AccountException;
import com.example.account.repository.AccountRepository;
import com.example.account.repository.AccountUserRepository;
import com.example.account.type.AccountStatus;
import com.example.account.type.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.account.type.AccountStatus.IN_USE;
import static com.example.account.type.AccountStatus.UNREGISTERED;
import static com.example.account.type.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final AccountUserRepository accountUserRepository;

    /**
     * 사용자가 있는지 조회하고,
     * 계좌 번호 생성하고,
     * 계좌를 저장하고, 그 정보를 넘김
     */
    @Transactional
    public AccountDto createAccount(Long userId, Long initialBalance){
        // 사용자가 있는지 조회하는 부분 - 없으면 AccountException 발생시킴
        AccountUser accountUser = accountUserRepository.findById(userId)
                .orElseThrow(()->new AccountException(USER_NOT_FOUND));

        // 아이디에 해당하는 사람의 계좌가 10개가 넘는지 확인
        validateCreateAccount(accountUser);

        // 계좌번호 생성하는 부분 : 가장 최근에 생성된 계좌의 계좌번호 +1
        String newAccountNumber = accountRepository.findFirstByOrderByIdDesc()
                .map(account -> (Integer.parseInt(account.getAccountNumber()))+1+"")
                .orElse("1000000000");

        // 강사님은 계속 한번 쓰이는 변수는 의미가 없다면서 return문을 엄청 길게 만드는데,
        // 그 이유가 중간에 다른 로직이 끼어들 수 있다는 것 때문!
        return AccountDto.fromEntity(accountRepository.save(
                Account.builder()
                        .accountUser(accountUser)
                        .accountStatus(IN_USE)
                        .accountNumber(newAccountNumber)
                        .balance(initialBalance)
                        .registeredAt(LocalDateTime.now())
                        .build()));
    }

    @Transactional
    public AccountDto deleteAccount(Long userId, String accountNumber) {
        // 유저를 조회해서 없으면 에러
        AccountUser accountUser = getAccountUser(userId);
        // 계좌를 조회해서 없으면 에러
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountException(ACCOUNT_NOT_FOUND));

        // 계좌 해지의 3가지 조건 (사용자==계좌 소유주 / 이미 해지된 계좌인지 / 잔액이 있는지) 검증
        validateDeleteAccount(accountUser, account);

        // 비지니스 로직 : 상태를 바꿔주고, 해지 시간을 현재 시간으로 바꿔줘야 함
        account.setAccountStatus(AccountStatus.UNREGISTERED);
        account.setUnRegisteredAt(LocalDateTime.now());

        // save를 통해서 원하는 값이 들어갔는지를 확인 - 굳이 delelte에서 필요한건 아니지만, 검증을 위해 추기한 코드
        accountRepository.save(account);

        return AccountDto.fromEntity(account);
    }


    private void validateDeleteAccount(AccountUser accountUser, Account account) {
        // 사용자와 계좌의 소유주가 같은지 확인
        if(accountUser.getId() != account.getAccountUser().getId()){
            throw new AccountException(USER_ACCOUNT_UN_MATCH);
        }
        // 계좌가 이미 해지되었는지 확인
        if(account.getAccountStatus()== AccountStatus.UNREGISTERED){
            throw new AccountException(ACCOUNT_ALREADY_UNREGISTERED);
        }
        // 잔액이 남아있는지 확인
        if (account.getBalance() > 0) {
            throw new AccountException(BALANCE_NOT_EMPTY);
        }
    }

    // 아이디에 해당하는 사람의 계좌가 10개가 넘는지 확인
    private void validateCreateAccount(AccountUser accountUser) {
        if(accountRepository.countByAccountUser(accountUser)>= 10){
            throw new AccountException(MAX_ACCOUNT_PER_USER_10);
        }
    }

    @Transactional
    public Account getAccount(Long id) {
        if(id < 0){
            throw new RuntimeException("Minus");
        }
        return accountRepository.findById(id).get();
    }

    private AccountUser getAccountUser(Long userId) {
        return accountUserRepository.findById(userId)
                .orElseThrow(() -> new AccountException(USER_NOT_FOUND));
    }

    @Transactional
    public List<AccountDto> getAccountsByUserId(Long userId) {
        AccountUser accountUser = accountUserRepository.findById(userId)
                .orElseThrow(() -> new AccountException(USER_NOT_FOUND));

        List<Account> accounts = accountRepository
                .findByAccountUser(accountUser);

        return accounts.stream()
                .map(AccountDto::fromEntity)
                .collect(Collectors.toList());
    }
}

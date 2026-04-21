package dev.thilinifernando.account_service.service;

import dev.thilinifernando.account_service.dto.AccountDetails;
import dev.thilinifernando.account_service.entity.Account;
import dev.thilinifernando.account_service.repo.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {

    private AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public AccountDetails getAccountDetails(String userId) {
        Optional<Account> account = accountRepository.findByUserId(userId);
        return account.map(value -> new AccountDetails(value.getUserId(), value.getBalance(),"lkr"))
                .orElseThrow(RuntimeException::new);
    }


}

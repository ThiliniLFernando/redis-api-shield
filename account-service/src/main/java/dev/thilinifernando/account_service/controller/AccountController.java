package dev.thilinifernando.account_service.controller;

import dev.thilinifernando.account_service.dto.AccountDetails;
import dev.thilinifernando.account_service.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private AccountService accountService;

    @Autowired
    private AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/{userId}")
    public AccountDetails getAccountDetails(@PathVariable String userId) {
        return accountService.getAccountDetails(userId);
    }
}

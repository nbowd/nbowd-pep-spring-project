package com.example.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.exception.DuplicateAccountUsernameException;
import com.example.exception.InvalidRequestParametersException;
import com.example.repository.AccountRepository;

@Service
@Transactional
public class AccountService {
    private AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public void register(Account newAccount) {
        if (newAccount.getUsername().equals("")) {
            throw new InvalidRequestParametersException("Username is not allowed to be blank.");
        }
        if (newAccount.getPassword().length() < 4) {
            throw new InvalidRequestParametersException("Password must be at least 4 characters long.");
        }
        
        List<Account> accountList = accountRepository.findAll();

        for (Account account: accountList) {
            if (newAccount.getUsername().equals(account.getUsername())) {
                throw new DuplicateAccountUsernameException("Username already exists.");
            }
        }
        this.accountRepository.save(newAccount);
    }
}

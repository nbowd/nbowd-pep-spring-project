package com.example.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.exception.InvalidMessageRequestException;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

@Service
@Transactional
public class MessageService {
    private MessageRepository messageRepository;
    private AccountRepository accountRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository) {
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }

    public void createMessage(Message newMessage) {
        if (newMessage.getMessageText().equals("")) {
            throw new InvalidMessageRequestException("Message text may not be blank.");
        }
        if (newMessage.getMessageText().length() > 255) {
            throw new InvalidMessageRequestException("Message text must be under 255 characters.");
        }

        List<Account> accountList = accountRepository.findAll();

        for (Account account: accountList) {
            int newId = newMessage.getPostedBy();
            int currId = account.getAccountId();
            if (newId == currId) {
                messageRepository.save(newMessage);
                return;
            }
        }
        throw new InvalidMessageRequestException("Message must be posted by existing user. Current postedBy is invalid.");

    }
}

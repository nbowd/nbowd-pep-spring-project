package com.example.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public List<Message> getMessageList() {
        return messageRepository.findAll();
    }

    public Optional<Message> getMessageById(int messageId) {
        return messageRepository.findById(messageId);
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

    public Optional<Integer> deleteMessage(int messageId) {
        Optional<Integer> rowsAffected = Optional.empty();

        int rows = messageRepository.deleteByMessageId(messageId);

        if (rows > 0) {
            rowsAffected = Optional.of(rows);
        }

        return rowsAffected;
    }

    public void updateMessage(int messageId, String messageText) {
        if (messageText.equals("")) {
            throw new InvalidMessageRequestException("Message text may not be blank.");
        }
        if (messageText.length() > 255) {
            throw new InvalidMessageRequestException("Message text must be under 255 characters.");
        }

        Message message = messageRepository.findById(messageId).orElseThrow(() -> new InvalidMessageRequestException("Message must exist to edit. Current messageId is invalid."));

        message.setMessageText(messageText);

        messageRepository.save(message);
    }
    
    public List<Message> getMessagesFromAccount(int accountId) {
        List<Message> accountMessages = messageRepository.findMessageByAccountId(accountId);

        return accountMessages;
    }
}

package com.example.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */

 @RestController
public class SocialMediaController {
    private AccountService accountService;
    private MessageService messageService;

    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    }

    @PostMapping("register")
    public ResponseEntity<Account> registerAccount(@RequestBody Account newAccount) {
        accountService.register(newAccount);
        return ResponseEntity.ok().body(newAccount);
    }

    @PostMapping("login")
    public ResponseEntity<Account> loginAccount(@RequestBody Account loginAccount) {
        Optional<Account> account = accountService.login(loginAccount);

        if (account.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok().body(account.get());
    }

    @GetMapping("messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        return ResponseEntity.ok().body(messageService.getMessageList());
    }

    @GetMapping("messages/{messageId}")
    public ResponseEntity<Message> findMessageById(@PathVariable int messageId) {
        Optional<Message> message = messageService.getMessageById(messageId);

        if (message.isEmpty()) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.ok().body(message.get());
    }

    @PostMapping("messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message newMessage) {
        messageService.createMessage(newMessage);
        return ResponseEntity.ok().body(newMessage);
    }

    @DeleteMapping("messages/{messageId}")
    public ResponseEntity<Integer> deleteMessageById(@PathVariable int messageId) {
        Optional<Integer> rowsAffected = messageService.deleteMessage(messageId);

        if (rowsAffected.isEmpty()) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.ok().body(rowsAffected.get());
    }
}

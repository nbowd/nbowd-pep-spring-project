package com.example.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

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

    @PatchMapping("messages/{messageId}")
    public ResponseEntity<Integer> updateMessageById(@PathVariable int messageId, @RequestBody String bodyProperties) throws JsonProcessingException {

        // I think there is a better way to do this with Spring Boot, but it looks like it involves some annotations in the Message model. 
        // But I didn't try them since we are not supposed to edit the models. I used this solution from my first project to parse a stringified JSON object.
        // If there is a correct Spring Boot way to retrieve individual properties from a request body that doesn't involve Message model annotations, I would like to know for my own understanding. 
        // I couldn't find that info in the lessons, as we just covered retrieving fully formed entities from the request body and individual properties in the path/headers.
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(bodyProperties);
        String messageText = rootNode.get("messageText").asText();

        messageService.updateMessage(messageId, messageText);
        return ResponseEntity.ok().body(1);
    }

    @GetMapping("accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getAllMessageFromAccount(@PathVariable int accountId) {
        List<Message> messageList = messageService.getMessagesFromAccount(accountId);

        return ResponseEntity.ok().body(messageList);
    }
}

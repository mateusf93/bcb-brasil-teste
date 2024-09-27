package br.com.bcbbrasil.controllers;


import br.com.bcbbrasil.dto.CostumerDTO;
import br.com.bcbbrasil.dto.MessageDTO;
import br.com.bcbbrasil.service.MessageServices;
import br.com.bcbbrasil.service.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserServices userServices;
    @Autowired
    MessageServices messageServices;

    @GetMapping("/profile")
    public ResponseEntity userInfo(@RequestHeader("Authorization") String authorizationHeader) throws Exception {
        CostumerDTO costumerDTO = userServices.getLoggedUserInfo(authorizationHeader);
        return ResponseEntity.ok().body(costumerDTO);
    }
    @GetMapping("/messages")
    public  ResponseEntity messageFromUser(@RequestHeader("Authorization") String authorizationHeader) throws Exception {
        List<MessageDTO>messageDTOS = messageServices.userMessages(authorizationHeader);
        return ResponseEntity.ok().body(messageDTOS);
    }
}

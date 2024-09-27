package br.com.bcbbrasil.controllers;

import br.com.bcbbrasil.dto.BalanceDTO;
import br.com.bcbbrasil.dto.MessageDTO;
import br.com.bcbbrasil.service.MessageServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/send")
public class MessageController {
    @Autowired
    MessageServices messageServices;

    @PostMapping("/message")
    public ResponseEntity sendMessage(@RequestHeader("Authorization") String authorizationHeader, @RequestBody @Validated MessageDTO messageDTO) throws Exception {
        try {
            messageServices.sendMessage(messageDTO, authorizationHeader);
            return ResponseEntity.ok().body("Mensagem enviada com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}

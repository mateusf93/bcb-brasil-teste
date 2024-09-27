package br.com.bcbbrasil.controllers;


import br.com.bcbbrasil.config.InvalidUserRoleException;
import br.com.bcbbrasil.dto.*;
import br.com.bcbbrasil.models.BalanceType;
import br.com.bcbbrasil.service.AuthService;
import br.com.bcbbrasil.service.BalanceServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/balance")
public class BalanceController {
    @Autowired
    BalanceServices balanceServices;
    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity registerBalance(@RequestBody @Validated BalanceDTO balanceDTO) throws InvalidUserRoleException {
        try {
            balanceServices.registerBalance(balanceDTO);
            return ResponseEntity.ok().body("Forma de pagamento cadastrado com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PutMapping("/addCredits/{userId}")
    public ResponseEntity addCredits(@PathVariable Long userId, @RequestBody @Validated AddCreditsDTO addCreditsDTO) {
        try {
            balanceServices.addCredits(addCreditsDTO, userId);
            return ResponseEntity.ok().body("Créditos adicionados com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/getBalance/{userId}")
    public ResponseEntity getBalanceByUser(@PathVariable Long userId) {
        try {
            BigDecimal currentBalance = balanceServices.getBalanceByUser(userId);
            HashMap<String, BigDecimal> response = new HashMap<>();
            response.put("Current Balance", currentBalance);
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/userInfo")
    public ResponseEntity getUsersInfo() {
        List<UserDTO> users = authService.getUsersInfo();
        return ResponseEntity.ok().body(users);
    }

    @PutMapping("/changeBalance/{userId}")
    public ResponseEntity changeBalance(@PathVariable Long userId, @RequestBody @Validated TotalBalanceDTO totalBalanceDTO) {
        try {
            balanceServices.changeTotalBalance(totalBalanceDTO, userId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/changeBalanceType/{userId}")
    public ResponseEntity changeBalanceType(@PathVariable Long userId, @RequestBody @Validated BalanceTypeDTO balanceTypeDTO) throws InvalidUserRoleException {
        try {
            balanceServices.changeBalanceType(balanceTypeDTO, userId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/getCostumerInfo/{userId}")
    public ResponseEntity getCostumerInfo(@PathVariable Long userId) throws InvalidUserRoleException {
        try {
            CostumerDTO costumerDTO = balanceServices.getCostumerInfo(userId);
            return ResponseEntity.ok().body(costumerDTO);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

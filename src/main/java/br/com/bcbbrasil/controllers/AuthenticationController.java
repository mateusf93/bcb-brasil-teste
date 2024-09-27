package br.com.bcbbrasil.controllers;


import br.com.bcbbrasil.dto.AuthenticationDTO;
import br.com.bcbbrasil.dto.LoginResponseDTO;
import br.com.bcbbrasil.dto.RegisterDTO;
import br.com.bcbbrasil.models.User;
import br.com.bcbbrasil.repository.UserRepository;
import br.com.bcbbrasil.service.AuthService;
import br.com.bcbbrasil.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("auth")
public class AuthenticationController {
    @Autowired
    private UserRepository repository;

    @Autowired
    AuthService authService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Validated AuthenticationDTO data) {
        try {
            LoginResponseDTO responseDTO = authService.login(data);
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Validated RegisterDTO data) {
        if (this.repository.findByEmail(data.email()) != null) {
            HashMap<String, String> msgError = new HashMap<>();
            msgError.put("error", "Usuário já cadastrado!");
            return ResponseEntity.badRequest().body(msgError);
        }
        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        authService.createUser(data, encryptedPassword);
        return ResponseEntity.ok().body("Usuário Criado com sucesso!");
    }


}
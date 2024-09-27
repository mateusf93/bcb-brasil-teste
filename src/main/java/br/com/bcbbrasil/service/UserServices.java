package br.com.bcbbrasil.service;

import br.com.bcbbrasil.dto.CostumerDTO;
import br.com.bcbbrasil.models.User;
import br.com.bcbbrasil.models.UserRole;
import br.com.bcbbrasil.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;



@Service
public class UserServices {

    @Autowired
    AuthService authService;
    @Autowired
    JwtService jwtService;
    @Autowired
    BalanceServices balanceServices;
    @Autowired
    UserRepository userRepository;
    @Value("${admin.email}")
    private String adminEmail;
    @Value("${admin.password}")
    private String adminPasword;

    public CostumerDTO getLoggedUserInfo(String token) throws Exception {

        token = token.replace("Bearer ", "");
        String email = jwtService.getLoggedUserEmail(token);
        User user = authService.loadUserByUsername(email);
        if (user.getRole().equals(UserRole.USER)) {
            return balanceServices.getCostumerInfo(user.getId());
        } else {
            throw new Exception("Este usuário não é do tipo cliente!");
        }
    }

    public void createAdminUserIfNotExists() throws Exception {

        User userExists = authService.loadUserByUsername(adminEmail);

        if (userExists == null) {

            User user = new User();
            user.setName("admin");
            user.setCnpj("1234567898");
            user.setCpfResponsavel("88849498");
            user.setEmail(adminEmail);
            user.setPassword(new BCryptPasswordEncoder().encode(adminPasword));
            user.setTelefone("3492025428");
            user.setRole(UserRole.ADMIN);
            user.setNomeEmpresa("Administrador");
            userRepository.save(user);
        }
        this.createPrePaidUser();
    }

    public void createPrePaidUser() throws Exception {

        User userExists = authService.loadUserByUsername("prepaiduser@bcbbrasil.com.br");
        try {
            if (userExists == null) {

                User user = new User();
                user.setName("prepaidUser");
                user.setCnpj("1234567899");
                user.setCpfResponsavel("888494989");
                user.setEmail("prepaiduser@bcbbrasil.com.br");
                user.setPassword(new BCryptPasswordEncoder().encode(adminPasword));
                user.setTelefone("34920254287");
                user.setRole(UserRole.USER);
                user.setNomeEmpresa("Pre-Paid User Interprises");
                userRepository.save(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

package br.com.bcbbrasil.config;

import br.com.bcbbrasil.service.AuthService;
import br.com.bcbbrasil.service.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AdminUserInitializer implements CommandLineRunner {

    @Autowired
    UserServices userServices;

    @Override
    public void run(String... args) throws Exception {
        userServices.createAdminUserIfNotExists();

    }
}

package br.com.bcbbrasil;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BcbBrasilApplication {

	public static void main(String[] args) {

		SpringApplication.run(BcbBrasilApplication.class, args);
	}

}

package br.com.bcbbrasil.config;

import br.com.bcbbrasil.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
public class SecurityConfig implements WebMvcConfigurer{
    @Autowired
    JwtRequestFilter jwtRequestFilter;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:4000")
                .allowedHeaders("Authorization", "Content-Type", "Accept")
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "PUT");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return  httpSecurity
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "auth/login").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "auth/register").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "balance/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "balance/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "balance/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "balance/*/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "balance/*/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "user/*").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "user/*/*").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "send/*").hasRole("USER")
                       .requestMatchers(HttpMethod.GET, "send/*").hasRole("USER")
                       .anyRequest().authenticated()
                )
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
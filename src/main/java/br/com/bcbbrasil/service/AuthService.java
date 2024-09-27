package br.com.bcbbrasil.service;

import br.com.bcbbrasil.dto.AuthenticationDTO;
import br.com.bcbbrasil.dto.LoginResponseDTO;
import br.com.bcbbrasil.dto.RegisterDTO;
import br.com.bcbbrasil.dto.UserDTO;
import br.com.bcbbrasil.models.User;
import br.com.bcbbrasil.models.UserRole;
import br.com.bcbbrasil.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AuthService implements UserDetailsService {

    @Autowired
    UserRepository repository;
    @Autowired
    public JwtService jwtService;
    @Autowired
    public UserRepository userRepository;



    @Override
    public User loadUserByUsername(String email) throws UsernameNotFoundException {
        return repository.findByEmail(email);
    }

    public LoginResponseDTO login(AuthenticationDTO data) throws Exception {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        User user = userRepository.findByEmail(data.email());
        if (!passwordEncoder.matches(data.password(), user.getPassword())) {
            throw new Exception("Senha inválida");
        }
        String token = jwtService.generateToken(user);
        return new LoginResponseDTO(token, user.getRole());
    }


    public void createUser(RegisterDTO registerData,String encryptedPassword){
        User user = new User();
        user.setTelefone(registerData.telefone());
        user.setName(registerData.name());
        user.setCnpj(registerData.cnpj());
        user.setEmail(registerData.email());
        user.setNomeEmpresa(registerData.nomeEmpresa());
        user.setCpfResponsavel(registerData.cpfResponsavel());
        user.setPassword(encryptedPassword);
        user.setRole(UserRole.USER);
        repository.save(user);
    }


    public User getUserById(Long id){
        Optional<User> user = userRepository.findById(id);
        return user.orElse(null);
    }
    public List<UserDTO> getUsersInfo(){
        List<UserDTO> userDTOS = new ArrayList<>();
        List<User> users = userRepository.findAll();
        for(User user : users){
          UserDTO userDTO = new UserDTO(user.getId(),user.getName(),user.getEmail(),user.getTelefone(),user.getCpfResponsavel(),user.getCnpj(),user.getNomeEmpresa());
          userDTOS.add(userDTO);
        }
        return  userDTOS;
    }
    public UserRole getRole(Long userId){
        Optional<User> user = userRepository.findById(userId);
        if(user.isPresent()){
            return user.get().getRole();
        }else{
            throw new RuntimeException("Usuário não encontrado!");
        }
    }

}
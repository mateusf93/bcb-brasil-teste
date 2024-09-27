package br.com.bcbbrasil.service;


import br.com.bcbbrasil.config.InvalidUserRoleException;
import br.com.bcbbrasil.dto.*;
import br.com.bcbbrasil.models.Balance;
import br.com.bcbbrasil.models.BalanceType;
import br.com.bcbbrasil.models.User;
import br.com.bcbbrasil.models.UserRole;
import br.com.bcbbrasil.repository.BalanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class BalanceServices {

    @Autowired
    BalanceRepository balanceRepository;

    @Autowired
    AuthService authService;

    public void registerBalance(BalanceDTO balanceDTO) throws InvalidUserRoleException {
        User user = authService.loadUserByUsername(balanceDTO.email());
        this.validateRole(user.getId());
        Balance balance = new Balance();
        balance.setBalanceType(balanceDTO.balanceType());
        balance.setCurrentBalance(new BigDecimal(0));
        balance.setTotalBalance(new BigDecimal(0));
        balance.setTotalSpent(new BigDecimal(0));
        balance.setUser(user);
        balanceRepository.save(balance);
    }
    public void updateTotalSpent(Balance balance, BigDecimal value){
        if(balance.getTotalSpent()== null){
            balance.setTotalSpent(value);
            balanceRepository.save(balance);
        }else {
            balance.setTotalSpent(balance.getTotalSpent().add(value));
            balanceRepository.save(balance);
        }
    }
    public void updateCurrentBalance(Balance balance, BigDecimal value){
        balance.setCurrentBalance(balance.getCurrentBalance().subtract(value));
        balanceRepository.save(balance);
    }

    public void addCredits(AddCreditsDTO addCreditsDTO, Long userId) throws Exception {
        this.validateRole(userId);
        Balance balance = balanceRepository.findBalanceByUserId(userId);
        if(balance.getBalanceType().equals(BalanceType.POSTPAID)){
            throw  new Exception("Só é possível inserir créditos para clientes com plano pré pagos!");
        }
        balance.setCurrentBalance(balance.getCurrentBalance().add(addCreditsDTO.currentBalance()));
        balanceRepository.save(balance);

    }

    public BigDecimal getBalanceByUser(Long userId) throws InvalidUserRoleException {
        this.validateRole(userId);
        Balance balance = balanceRepository.findBalanceByUserId(userId);
        return balance.getCurrentBalance();
    }
    public Balance getInfoBalanceByUser(Long userId) throws InvalidUserRoleException {
        this.validateRole(userId);
        return balanceRepository.findBalanceByUserId(userId);
        }

    public void changeTotalBalance(TotalBalanceDTO totalBalanceDTO, Long userId) throws Exception {
        this.validateRole(userId);
        Balance balance = balanceRepository.findBalanceByUserId(userId);
        if(balance.getBalanceType().equals(BalanceType.PREPAID)){
            throw  new Exception("Só é possível alterar os limites de crédito para clientes pós pagos!");
        }
        balance.setTotalBalance(totalBalanceDTO.totalBalance());
        balanceRepository.save(balance);
    }

    public void changeBalanceType(BalanceTypeDTO balanceTypeDTO, Long userId) throws InvalidUserRoleException {
        this.validateRole(userId);
       Balance balance = balanceRepository.findBalanceByUserId(userId);
       balance.setBalanceType(balanceTypeDTO.balanceType());
       balanceRepository.save(balance);
    }

    public CostumerDTO getCostumerInfo(Long userId) throws InvalidUserRoleException {
        this.validateRole(userId);
        User user = authService.getUserById(userId);
        Balance balance = balanceRepository.findBalanceByUserId(userId);
        return new CostumerDTO(userId, user.getName(), user.getEmail(), user.getTelefone(), user.getCpfResponsavel(), user.getCnpj(),user.getNomeEmpresa(),balance.getBalanceType(),balance.getTotalBalance(),balance.getCurrentBalance(),balance.getTotalSpent());

    }
    public void validateRole(Long userId) throws InvalidUserRoleException {
        UserRole userRole = authService.getRole(userId);
        if(!userRole.equals(UserRole.USER)){
            throw new InvalidUserRoleException("Usuário não é do tipo cliente!");
        }
    }



}

package br.com.bcbbrasil.repository;

import br.com.bcbbrasil.models.Balance;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BalanceRepository extends JpaRepository<Balance, Long> {

    Balance findBalanceByUserId(Long userId);
}

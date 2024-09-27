package br.com.bcbbrasil.dto;

import br.com.bcbbrasil.models.BalanceType;

import java.math.BigDecimal;

public record CostumerDTO(Long userId, String name, String email, String telefone, String cpfResponsavel, String cnpj, String nomeEmpresa,
                          BalanceType balanceType, BigDecimal totalBalance, BigDecimal currentBalance, BigDecimal totalSpent) {
}

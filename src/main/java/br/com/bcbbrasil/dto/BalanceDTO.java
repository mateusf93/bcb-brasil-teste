package br.com.bcbbrasil.dto;

import br.com.bcbbrasil.models.Balance;
import br.com.bcbbrasil.models.BalanceType;

import java.math.BigDecimal;

public record BalanceDTO(String email, BalanceType balanceType) {



}

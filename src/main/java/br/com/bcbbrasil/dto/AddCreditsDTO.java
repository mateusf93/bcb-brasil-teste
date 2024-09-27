package br.com.bcbbrasil.dto;

import java.math.BigDecimal;

public record AddCreditsDTO(String email, BigDecimal currentBalance) {
}

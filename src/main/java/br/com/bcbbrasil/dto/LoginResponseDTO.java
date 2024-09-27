package br.com.bcbbrasil.dto;

import br.com.bcbbrasil.models.UserRole;

public record LoginResponseDTO(String token, UserRole role) {
}
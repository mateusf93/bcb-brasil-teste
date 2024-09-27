package br.com.bcbbrasil.dto;

import br.com.bcbbrasil.models.SendType;

public record MessageDTO(String textMessage, String telefoneDest, SendType sendType) {
}

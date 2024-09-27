package br.com.bcbbrasil.dto;


import br.com.bcbbrasil.models.UserRole;

public record RegisterDTO(String name, String email, String telefone, String cpfResponsavel, String cnpj, String nomeEmpresa, String password) {



}

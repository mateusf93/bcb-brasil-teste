package br.com.bcbbrasil.dto;

public record UserDTO(Long id,String name, String email, String telefone, String cpfResponsavel, String cnpj, String nomeEmpresa) {
}

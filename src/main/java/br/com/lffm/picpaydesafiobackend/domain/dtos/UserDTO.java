package br.com.lffm.picpaydesafiobackend.domain.dtos;

import br.com.lffm.picpaydesafiobackend.domain.user.UserType;

import java.math.BigDecimal;

public record UserDTO(String firstName, String lastName, String document, BigDecimal balance, String email, String password, UserType userType) {
}

package br.com.lffm.picpaydesafiobackend.domain.services;

import br.com.lffm.picpaydesafiobackend.domain.dtos.UserDTO;
import br.com.lffm.picpaydesafiobackend.domain.user.User;
import br.com.lffm.picpaydesafiobackend.domain.user.UserType;
import br.com.lffm.picpaydesafiobackend.respositories.UserRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRespository userRespository;

    public void validateTransaction(User sender, BigDecimal amount) throws Exception {
        if(sender.getUserType() != UserType.COMMON) {
            throw new Exception("Usuário do tipo Logista não está autorizado a realizar transação.");
        }

        if(sender.getBalance().compareTo(amount) < 0) {
            throw new Exception("Saldo insuficiente.");
        }
    }

    public User findByUserId(Long id) throws Exception {
       return this.userRespository.findUserById(id).orElseThrow(() -> new Exception("Usuário não encontrado"));
    }

    public void saveUser(User user) {
        this.userRespository.save(user);
    }

    public User createUser(UserDTO user) {
        User newUser = new User(user);
        this.userRespository.save(newUser);
        return newUser;
    }

    public List<User> getAllUsers() {
       return this.userRespository.findAll();
    }
}

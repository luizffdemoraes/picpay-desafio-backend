package br.com.lffm.picpaydesafiobackend.domain.services;

import br.com.lffm.picpaydesafiobackend.domain.dtos.TransactionDTO;
import br.com.lffm.picpaydesafiobackend.domain.transaction.Transaction;
import br.com.lffm.picpaydesafiobackend.domain.user.User;
import br.com.lffm.picpaydesafiobackend.respositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class TransactionService {
    @Autowired
    private UserService userService;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private NotificationService notificationService;

    public Transaction createTransaction(TransactionDTO transaction) throws Exception {
        User sender = this.userService.findByUserId(transaction.senderId());
        User receiver = this.userService.findByUserId(transaction.receiverId());

        userService.validateTransaction(sender, transaction.value());

        boolean isAuthorized = this.authorizeTransaction(sender, transaction.value());
        if (!isAuthorized) {
            throw new Exception("Transação não autorizada");
        }

        Transaction newTransaction = new Transaction();
        newTransaction.setAmount(transaction.value());
        newTransaction.setSender(sender);
        newTransaction.setReceiver(receiver);
        newTransaction.setTimestamp(LocalDateTime.now());

        sender.setBalance(sender.getBalance().subtract(transaction.value()));
        receiver.setBalance(receiver.getBalance().add(transaction.value()));

        this.transactionRepository.save(newTransaction);
        this.userService.saveUser(sender);
        this.userService.saveUser(receiver);

        notificationService.sendNotification(sender, "Transação realizada com sucesso.");
        notificationService.sendNotification(receiver, "Transação recebida com sucesso.");

        return newTransaction;
    }

    public boolean authorizeTransaction(User sender, BigDecimal value) {
       ResponseEntity<Map> authorizationResponse = restTemplate.getForEntity("https://run.mocky.io/v3/5794d450-d2e2-4412-8131-73d0293ac1cc", Map.class);

       return authorizationResponse.getStatusCode().equals(HttpStatus.OK) && authorizationResponse.getBody().get("message").equals("Autorizado");
    }
}

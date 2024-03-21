package br.com.lffm.picpaydesafiobackend.transaction;

import br.com.lffm.picpaydesafiobackend.transaction.InvalidTransactionException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class TransactionExceptionHandler {

    @ExceptionHandler(InvalidTransactionException.class)
    public ResponseEntity<Object> handlerInvalidTransaction(InvalidTransactionException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}

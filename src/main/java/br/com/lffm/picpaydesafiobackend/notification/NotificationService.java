package br.com.lffm.picpaydesafiobackend.notification;

import br.com.lffm.picpaydesafiobackend.transaction.Transaction;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    /*
     * Funcionamento assincrono enviado de forma eventual utilizando mensageria
     */
    public void notify(Transaction transaction) {

    }
}

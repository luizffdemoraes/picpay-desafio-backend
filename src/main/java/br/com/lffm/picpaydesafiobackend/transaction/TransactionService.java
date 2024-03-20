package br.com.lffm.picpaydesafiobackend.transaction;

import br.com.lffm.picpaydesafiobackend.authorization.AuthorizerService;
import br.com.lffm.picpaydesafiobackend.notification.NotificationService;
import br.com.lffm.picpaydesafiobackend.wallet.Wallet;
import br.com.lffm.picpaydesafiobackend.wallet.WalletRepository;
import br.com.lffm.picpaydesafiobackend.wallet.WalletType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;
    private final AuthorizerService authorizerService;
    private final NotificationService notificationService;


    public TransactionService(TransactionRepository transactionRepository, WalletRepository walletRepository, AuthorizerService authorizerService, NotificationService notificationService) {
        this.transactionRepository = transactionRepository;
        this.walletRepository = walletRepository;
        this.authorizerService = authorizerService;
        this.notificationService = notificationService;
    }

    @Transactional
    public Transaction create(Transaction transaction) {
        // 1 - Validar
        validate(transaction);

        // 2 - Criar a transação
        var newTransaction = transactionRepository.save(transaction);

        // 3 - Debitar da Carteira
        var wallet = walletRepository.findById(transaction.payee()).get();
        walletRepository.save(wallet.debit(transaction.value()));

        // 4 - Chamar serviços externos
        // authorize transaction
        authorizerService.authorize(transaction);

        // 5 - Notificação
        notificationService.notify(transaction);


        return newTransaction;
    }

    /*
     * - the payer has a common wallet
     * - the payer has enough balance
     * - the payer is not the payee
     */
    private void validate(Transaction transaction) {
        walletRepository.findById(transaction.payee())
            .map(payee -> walletRepository.findById(transaction.payer())
                .map(payer -> isTransactionValid(transaction, payer) ? transaction : null)
                .orElseThrow(() -> new InvalidTransactionException("Invalid transaction - %s".formatted(transaction))))
            .orElseThrow(() -> new InvalidTransactionException("Invalid transaction - %s".formatted(transaction)));
    }

    private static boolean isTransactionValid(Transaction transaction, Wallet payer) {
        return payer.type() == WalletType.COMUM.getValue() &&
            payer.balance().compareTo(transaction.value()) >= 0 &&
            !payer.id().equals(transaction.payee());
    }
}

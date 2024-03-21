package br.com.lffm.picpaydesafiobackend.respositories;

import br.com.lffm.picpaydesafiobackend.domain.transaction.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}

package am.bdgexame.bancapplication.service;

import am.bdgexame.bancapplication.entity.AccountEntity;
import am.bdgexame.bancapplication.entity.TransactionEntity;
import am.bdgexame.bancapplication.entity.UserEntity;
import am.bdgexame.bancapplication.model.Status;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;


public interface TransactionService {
    Optional<TransactionEntity> getById(long id);

    Set<TransactionEntity> getAll();

    TransactionEntity save(TransactionEntity transactionEntity);

    TransactionEntity update(TransactionEntity transactionEntity);

    List<TransactionEntity> getUserTransactions(UserEntity userEntity);

    List<TransactionEntity> getUserTransactionsByDate(UserEntity userEntity, LocalDate dateTime);

    List<TransactionEntity> getTransactionsByFilter(UserEntity userEntity, LocalDate localDateTime, Status status);
}

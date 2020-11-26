package am.bdgexame.bancapplication.service.serviceimpl;

import am.bdgexame.bancapplication.entity.TransactionEntity;
import am.bdgexame.bancapplication.entity.UserEntity;
import am.bdgexame.bancapplication.exceptions.BadRequestException;
import am.bdgexame.bancapplication.exceptions.ConflictException;
import am.bdgexame.bancapplication.model.Status;
import am.bdgexame.bancapplication.repo.TransactionRepo;
import am.bdgexame.bancapplication.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepo transactionRepo;

    @Autowired
    public TransactionServiceImpl(TransactionRepo transactionRepo) {
        this.transactionRepo = transactionRepo;
    }

    @Override
    public Optional<TransactionEntity> getById(long id) {
        Optional<TransactionEntity> transactionEntity = transactionRepo.findById(id);
        return transactionEntity;
    }

    @Override
    public Set<TransactionEntity> getAll() {
        Set<TransactionEntity> transactionEntities = new HashSet<>();
        Iterable<TransactionEntity> transactionEntities1 = transactionRepo.findAll();
        if (transactionEntities1 != null) {
            for (TransactionEntity t : transactionEntities1) {
                transactionEntities.add(t);
            }
            return transactionEntities;
        }
        return null;
    }

    @Override
    public TransactionEntity save(TransactionEntity transactionEntity) {
        if (transactionEntity.getStatus() == null) {
            throw new ConflictException("It is requied to mention status of transaction.");
        }
        if (transactionEntity.getType() == null) {
            throw new BadRequestException("It is required to mention the type of transaction.");
        }
        if (transactionEntity.getAccountEntity() == null) {
            throw new ConflictException("It is required to mention account for transaction.");
        }
        if (transactionEntity.getUserEntity() == null) {
            throw new ConflictException("It is required to mention user that make a transaction.");
        }
        transactionEntity = transactionRepo.save(transactionEntity);
        return transactionEntity;
    }

    @Override
    public List<TransactionEntity> getUserTransactions(UserEntity userEntity) {
        return transactionRepo.getAllByUserEntity(userEntity);
    }

    @Override
    public List<TransactionEntity> getUserTransactionsByDate(UserEntity userEntity, LocalDate dateTime) {
        return transactionRepo.getAllByUserEntityAndTransactionDate(userEntity, dateTime);
    }

    @Override
    public List<TransactionEntity> getTransactionsByFilter(UserEntity userEntity, LocalDate localDateTime, Status status) {
        return transactionRepo.getAllByUserEntityAndTransactionDateAndStatus(userEntity, localDateTime, status);
    }

    @Override
    public TransactionEntity update(TransactionEntity transactionEntity) {
        return transactionRepo.save(transactionEntity);
    }
}

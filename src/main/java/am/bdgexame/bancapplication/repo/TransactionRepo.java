package am.bdgexame.bancapplication.repo;

import am.bdgexame.bancapplication.entity.TransactionEntity;
import am.bdgexame.bancapplication.entity.UserEntity;
import am.bdgexame.bancapplication.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepo extends JpaRepository<TransactionEntity, Long> {
    List<TransactionEntity> getAllByUserEntity(UserEntity userEntity);

    List<TransactionEntity> getAllByUserEntityAndTransactionDate(UserEntity userEntity, LocalDate dateTime);

    List<TransactionEntity> getAllByUserEntityAndTransactionDateAndStatus(UserEntity userEntity, LocalDate dateTime, Status status);
}

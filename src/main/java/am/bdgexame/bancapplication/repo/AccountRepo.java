package am.bdgexame.bancapplication.repo;

import am.bdgexame.bancapplication.entity.AccountEntity;
import am.bdgexame.bancapplication.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepo extends JpaRepository<AccountEntity, Long> {
    AccountEntity findAccountEntityByAccessCodeAndAccountNumber(Long accessCode, Long accountNumber);

    AccountEntity findAccountEntityByAccountIdAndUserEntity(Long accountId, UserEntity userEntity);

    List<AccountEntity> findAccountEntitiesByUserEntity(UserEntity userEntity);

    Boolean existsByAccessCodeAndAccountNumber(Long accessCode, Long accountNumber);
}

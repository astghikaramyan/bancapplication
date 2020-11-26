package am.bdgexame.bancapplication.service;

import am.bdgexame.bancapplication.dao.AccountDao;
import am.bdgexame.bancapplication.entity.AccountEntity;
import am.bdgexame.bancapplication.entity.UserEntity;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface AccountService {
    Optional<AccountEntity> getById(long id);

    Set<AccountEntity> getAll();

    AccountEntity save(AccountEntity accountEntity);

    void delete(long id);

    AccountEntity findAccountByAccessCodeAndAccountNumber(Long accessCode, Long accountNumber);

    AccountEntity getAccountByAccountIdAndUser(Long accountId, UserEntity userEntity);

    List<AccountEntity> getUserAccounts(UserEntity userEntity);

    AccountEntity update(AccountEntity accountEntity);
}

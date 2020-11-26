package am.bdgexame.bancapplication.service.serviceimpl;

import am.bdgexame.bancapplication.entity.AccountEntity;
import am.bdgexame.bancapplication.entity.UserEntity;
import am.bdgexame.bancapplication.exceptions.BadRequestException;
import am.bdgexame.bancapplication.exceptions.ConflictException;
import am.bdgexame.bancapplication.repo.AccountRepo;
import am.bdgexame.bancapplication.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepo accountRepo;

    @Autowired
    public AccountServiceImpl(AccountRepo accountRepo) {
        this.accountRepo = accountRepo;
    }

    @Override
    public Optional<AccountEntity> getById(long id) {
        Optional<AccountEntity> accountEntity = this.accountRepo.findById(id);
        return accountEntity;
    }

    @Override
    public Set<AccountEntity> getAll() {
        Set<AccountEntity> accountEntities = new HashSet<>();
        Iterable<AccountEntity> accountEntities1 = this.accountRepo.findAll();
        if (accountEntities1 != null) {
            for (AccountEntity a : accountEntities1) {
                accountEntities.add(a);
            }
            return accountEntities;
        }
        return null;
    }

    @Override
    public AccountEntity save(AccountEntity accountEntity) {
        if (accountRepo.existsByAccessCodeAndAccountNumber(accountEntity.getAccessCode(), accountEntity.getAccountNumber())) {
            throw new ConflictException("Account with such account number and access code exists.");
        }
        accountEntity = this.accountRepo.save(accountEntity);
        return accountEntity;
    }

    @Override
    public void delete(long id) {
        Optional<AccountEntity> accountEntity = accountRepo.findById(id);
        if (accountEntity.isPresent()) {
            accountRepo.deleteById(id);
        }
    }

    @Override
    public AccountEntity findAccountByAccessCodeAndAccountNumber(Long accessCode, Long accountNumber) {
        return accountRepo.findAccountEntityByAccessCodeAndAccountNumber(accessCode, accountNumber);
    }

    @Override
    public AccountEntity getAccountByAccountIdAndUser(Long accountId, UserEntity userEntity) {
        return accountRepo.findAccountEntityByAccountIdAndUserEntity(accountId, userEntity);
    }

    @Override
    public List<AccountEntity> getUserAccounts(UserEntity userEntity) {
        return accountRepo.findAccountEntitiesByUserEntity(userEntity);
    }

    @Override
    public AccountEntity update(AccountEntity accountEntity) {
        return accountRepo.save(accountEntity);
    }
}

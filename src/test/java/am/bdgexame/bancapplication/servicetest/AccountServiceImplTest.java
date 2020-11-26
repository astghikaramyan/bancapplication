package am.bdgexame.bancapplication.servicetest;

import am.bdgexame.bancapplication.entity.AccountEntity;
import am.bdgexame.bancapplication.entity.UserEntity;
import am.bdgexame.bancapplication.exceptions.ConflictException;
import am.bdgexame.bancapplication.repo.AccountRepo;
import am.bdgexame.bancapplication.service.serviceimpl.AccountServiceImpl;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.junit.Assume;

import java.util.*;

import static org.mockito.BDDMockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceImplTest {

    @Mock
    private AccountRepo accountRepo;

    @InjectMocks
    private AccountServiceImpl accountService;

    @Test
    public void get_by_id_test() {
        final Long id = 1L;
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setAccountId(id);
        given(accountRepo.findById(id)).willReturn(Optional.of(accountEntity));
        final Optional<AccountEntity> excepted = accountService.getById(id);
        Assume.assumeNotNull(excepted);
    }

    @Test
    public void get_all_test() {
        List<AccountEntity> list = new LinkedList<>();
        Set<AccountEntity> accountEntitySet = new HashSet<>();
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setAccountId(1L);
        accountEntity.setAccountNumber(5555216531L);
        accountEntity.setAccessCode(2564L);
        AccountEntity accountEntity1 = new AccountEntity();
        accountEntity1.setAccountId(2L);
        accountEntity.setAccountNumber(555567L);
        accountEntity.setAccessCode(2556L);
        accountEntitySet.add(accountEntity);
        accountEntitySet.add(accountEntity1);
        list.addAll(accountEntitySet);
        given(accountRepo.findAll()).willReturn(list);
        Set<AccountEntity> excepted = accountService.getAll();
        Assertions.assertEquals(excepted, accountEntitySet);
    }

    @Test
    public void when_save_account_with_existing_accountNumber_and_accessCode_it_will_throw_error() {

        Assertions.assertThrows(ConflictException.class, () -> {
            AccountEntity accountEntity = new AccountEntity();
            accountEntity.setAccountNumber(55552476L);
            accountEntity.setAccessCode(5555L);
            accountEntity.setAccountId(1L);
            given(accountRepo.existsByAccessCodeAndAccountNumber(accountEntity.getAccessCode(), accountEntity.getAccountNumber())).
                    willReturn(true);
            accountService.save(accountEntity);
        });
        verify(accountRepo, never()).save(any(AccountEntity.class));
    }

    @Test
    public void when_save_account_it_should_be_done_successfully() {
        AccountEntity accountEntity = new AccountEntity();
        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(1L);
        accountEntity.setAccessCode(5455L);
        accountEntity.setAccountNumber(273673458L);
        accountEntity.setBalance(32L);
        accountEntity.setUserEntity(userEntity);
        given(accountRepo.existsByAccessCodeAndAccountNumber(accountEntity.getAccessCode(), accountEntity.getAccountNumber()))
                .willReturn(false);
        given(accountRepo.save(accountEntity)).willAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        AccountEntity accountEntity1 = accountService.save(accountEntity);
        Assume.assumeNotNull(accountEntity1);
        verify(accountRepo).save(any(AccountEntity.class));
    }

    @Test
    public void update_account() {
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setAccountNumber(24573482L);
        accountEntity.setAccessCode(5555L);
        accountEntity.setAccountId(1L);
        given(accountRepo.save(accountEntity)).willReturn(accountEntity);
        AccountEntity accountEntity1 = accountService.update(accountEntity);
        Assume.assumeNotNull(accountEntity1);
        verify(accountRepo).save(any(AccountEntity.class));
    }

    @Test
    public void find_account_by_accessCode_and_accountNumber() {
        final Long accessCode = 555L;
        final Long accountNumber = 576855L;
        AccountEntity accountEntity = new AccountEntity(accessCode, accountNumber);
        given(accountRepo.findAccountEntityByAccessCodeAndAccountNumber(accessCode, accountNumber))
                .willReturn(accountEntity);
        AccountEntity excepted = accountService.findAccountByAccessCodeAndAccountNumber(accessCode, accountNumber);
        Assertions.assertEquals(excepted, accountEntity);
    }

    @Test
    public void get_accounts_of_user_test() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(1L);
        Set<AccountEntity> accountEntities = new HashSet<>();
        AccountEntity accountEntity = new AccountEntity(5555L, 56588L);
        accountEntity.setAccountId(1L);
        AccountEntity accountEntity1 = new AccountEntity(5555L, 56588L);
        accountEntity1.setAccountId(2L);
        accountEntities.add(accountEntity);
        accountEntities.add(accountEntity1);
        userEntity.setAccountEntitySet(accountEntities);
        List<AccountEntity> list = new LinkedList<>();
        list.addAll(accountEntities);
        given(accountRepo.findAccountEntitiesByUserEntity(userEntity)).willReturn(list);
        List<AccountEntity> excepted = accountService.getUserAccounts(userEntity);
        Assertions.assertEquals(excepted, list);
    }

    @Test
    public void get_account_by_accountId_and_user() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(1L);
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setAccountId(2L);
        accountEntity.setUserEntity(userEntity);
        Set<AccountEntity> accountEntities = new HashSet<>();
        accountEntities.add(accountEntity);
        userEntity.setAccountEntitySet(accountEntities);
        given(accountRepo.findAccountEntityByAccountIdAndUserEntity(2L, userEntity))
                .willReturn(accountEntity);
        AccountEntity excepted = accountService.getAccountByAccountIdAndUser(2L, userEntity);
        Assertions.assertEquals(excepted, accountEntity);
    }

    @Test
    public void get_user_accounts_test() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(1L);
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setAccountId(2L);
        accountEntity.setAccessCode(5555L);
        accountEntity.setAccountNumber(53533555L);
        AccountEntity accountEntity1 = new AccountEntity();
        accountEntity1.setAccountId(3L);
        accountEntity1.setAccessCode(68256L);
        accountEntity1.setAccountNumber(3462869L);
        accountEntity.setUserEntity(userEntity);
        accountEntity1.setUserEntity(userEntity);
        Set<AccountEntity> accountEntities = new HashSet<>();
        accountEntities.add(accountEntity);
        accountEntities.add(accountEntity1);
        userEntity.setAccountEntitySet(accountEntities);
        List<AccountEntity> list = new LinkedList<>();
        list.addAll(accountEntities);
        given(accountRepo.findAccountEntitiesByUserEntity(userEntity)).willReturn(list);
        List<AccountEntity> excepted = accountService.getUserAccounts(userEntity);
        Assertions.assertEquals(excepted, list);
    }

    @Test
    public void delete_test() {
        final Long id = 1L;
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setAccountId(id);
        given(accountRepo.findById(id)).willReturn(Optional.of(accountEntity));
        accountService.delete(id);
        accountService.delete(id);
        verify(accountRepo, times(2)).deleteById(id);
    }
}

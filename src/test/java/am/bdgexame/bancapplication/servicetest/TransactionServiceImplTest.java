package am.bdgexame.bancapplication.servicetest;

import am.bdgexame.bancapplication.entity.AccountEntity;
import am.bdgexame.bancapplication.entity.TransactionEntity;
import am.bdgexame.bancapplication.entity.UserEntity;
import am.bdgexame.bancapplication.exceptions.BadRequestException;
import am.bdgexame.bancapplication.exceptions.ConflictException;
import am.bdgexame.bancapplication.model.Status;
import am.bdgexame.bancapplication.model.Type;
import am.bdgexame.bancapplication.repo.TransactionRepo;
import am.bdgexame.bancapplication.service.serviceimpl.TransactionServiceImpl;
import org.junit.Assume;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.*;

import static org.mockito.BDDMockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TransactionServiceImplTest {

    @Mock
    private TransactionRepo transactionRepo;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Test
    public void when_save_transaction_it_should_be_done_successfully() {
        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setStatus(Status.PENDING);
        transactionEntity.setType(Type.DEPOSIT);
        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(1L);
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setAccountId(1L);
        Set<AccountEntity> accountEntities = new HashSet<>();
        accountEntities.add(accountEntity);
        userEntity.setAccountEntitySet(accountEntities);
        accountEntity.setUserEntity(userEntity);
        transactionEntity.setAccountEntity(accountEntity);
        transactionEntity.setUserEntity(userEntity);
        Set<TransactionEntity> transactionEntities = new HashSet<>();
        transactionEntities.add(transactionEntity);
        userEntity.setTransactionEntitySet(transactionEntities);
        given(transactionRepo.save(transactionEntity)).willAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
        TransactionEntity transactionEntity1 = transactionService.save(transactionEntity);
        Assume.assumeNotNull(transactionEntity1);
        verify(transactionRepo).save(any(TransactionEntity.class));
    }

    @Test
    public void when_save_transaction_without_status_it_should_throw_error() {
        Assertions.assertThrows(ConflictException.class, () -> {
            TransactionEntity transactionEntity = new TransactionEntity();
            UserEntity userEntity = new UserEntity();
            userEntity.setUserId(1L);
            AccountEntity accountEntity = new AccountEntity();
            accountEntity.setAccountId(1L);
            transactionEntity.setUserEntity(userEntity);
            transactionEntity.setAccountEntity(accountEntity);
            transactionEntity.setType(Type.DEPOSIT);
            transactionService.save(transactionEntity);
        });
    }

    @Test
    public void when_save_transaction_without_type_it_should_throw_error() {
        Assertions.assertThrows(BadRequestException.class, () -> {
            TransactionEntity transactionEntity = new TransactionEntity();
            UserEntity userEntity = new UserEntity();
            userEntity.setUserId(1L);
            AccountEntity accountEntity = new AccountEntity();
            accountEntity.setAccountId(1L);
            transactionEntity.setUserEntity(userEntity);
            transactionEntity.setAccountEntity(accountEntity);
            transactionEntity.setStatus(Status.PENDING);
            transactionService.save(transactionEntity);
        });
    }

    @Test
    public void when_save_transaction_without_user_it_should_throw_error() {
        Assertions.assertThrows(ConflictException.class, () -> {
            TransactionEntity transactionEntity = new TransactionEntity();
            AccountEntity accountEntity = new AccountEntity();
            accountEntity.setAccountId(1L);
            transactionEntity.setAccountEntity(accountEntity);
            transactionEntity.setStatus(Status.PENDING);
            transactionEntity.setType(Type.DEPOSIT);
            transactionService.save(transactionEntity);
        });
    }

    @Test
    public void when_save_transaction_without_account_it_should_throw_error() {
        Assertions.assertThrows(ConflictException.class, () -> {
            TransactionEntity transactionEntity = new TransactionEntity();
            UserEntity userEntity = new UserEntity();
            userEntity.setUserId(2L);
            transactionEntity.setUserEntity(userEntity);
            transactionEntity.setStatus(Status.PENDING);
            transactionEntity.setType(Type.DEPOSIT);
            transactionService.save(transactionEntity);
        });
    }

    @Test
    public void get_by_id_test() {
        final Long id = 1L;
        final TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setTransactionId(id);
        transactionEntity.setType(Type.DEPOSIT);
        transactionEntity.setStatus(Status.PENDING);
        given(transactionRepo.findById(id)).willReturn(Optional.of(transactionEntity));
        final Optional<TransactionEntity> expected = transactionService.getById(id);
        Assume.assumeNotNull(expected);
    }

    @Test
    public void get_all_test() {
        List<TransactionEntity> transactionEntities = new LinkedList<>();
        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(1L);
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setAccountId(1L);

        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setTransactionId(1L);
        transactionEntity.setStatus(Status.PENDING);
        transactionEntity.setType(Type.DEPOSIT);
        transactionEntity.setUserEntity(userEntity);
        transactionEntity.setAccountEntity(accountEntity);

        TransactionEntity transactionEntity1 = new TransactionEntity();
        transactionEntity1.setTransactionId(2L);
        transactionEntity1.setStatus(Status.PENDING);
        transactionEntity1.setType(Type.DEPOSIT);
        transactionEntity1.setUserEntity(userEntity);
        transactionEntity1.setAccountEntity(accountEntity);

        transactionEntities.add(transactionEntity);
        transactionEntities.add(transactionEntity1);

        given(transactionRepo.findAll()).willReturn(transactionEntities);

        Set<TransactionEntity> transactionEntities1 = new HashSet<>();
        transactionEntities1.addAll(transactionEntities);

        Set<TransactionEntity> excepted = transactionService.getAll();

        Assertions.assertEquals(excepted, transactionEntities1);

    }

    @Test
    public void get_user_transactions() {
        Set<TransactionEntity> transactionEntities = new HashSet<>();
        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setTransactionId(1L);

        TransactionEntity transactionEntity1 = new TransactionEntity();
        transactionEntity1.setTransactionId(2L);

        transactionEntities.add(transactionEntity);
        transactionEntities.add(transactionEntity1);

        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(1L);
        userEntity.setTransactionEntitySet(transactionEntities);

        List<TransactionEntity> transactionEntities1 = new LinkedList<>();
        transactionEntities1.addAll(transactionEntities);

        given(transactionRepo.getAllByUserEntity(userEntity)).willReturn(transactionEntities1);

        List<TransactionEntity> excepted = transactionService.getUserTransactions(userEntity);

        Assertions.assertEquals(excepted, transactionEntities1);
    }

    @Test
    public void get_user_transactions_by_date_test() {
        Set<TransactionEntity> transactionEntities = new HashSet<>();
        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setTransactionId(1L);
        LocalDate date = LocalDate.now();
        transactionEntity.setTransactionDate(date);

        TransactionEntity transactionEntity1 = new TransactionEntity();
        transactionEntity1.setTransactionId(2L);
        transactionEntity.setTransactionDate(date);

        transactionEntities.add(transactionEntity);
        transactionEntities.add(transactionEntity1);

        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(1L);
        userEntity.setTransactionEntitySet(transactionEntities);

        List<TransactionEntity> list = new LinkedList<>();
        list.addAll(transactionEntities);
        given(transactionRepo.getAllByUserEntityAndTransactionDate(userEntity, date)).willReturn(list);

        List<TransactionEntity> excepted = transactionService.getUserTransactionsByDate(userEntity, date);

        Assertions.assertEquals(excepted, list);
    }

    @Test
    public void get_user_transactions_by_filter_test() {
        Set<TransactionEntity> transactionEntities = new HashSet<>();
        Set<TransactionEntity> excepted = new HashSet<>();

        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setTransactionId(1L);
        transactionEntity.setStatus(Status.PENDING);
        LocalDate date = LocalDate.now();
        transactionEntity.setTransactionDate(date);

        TransactionEntity transactionEntity1 = new TransactionEntity();
        transactionEntity1.setTransactionId(2L);
        transactionEntity.setStatus(Status.PENDING);
        transactionEntity.setTransactionDate(date);

        TransactionEntity transactionEntity2 = new TransactionEntity();
        transactionEntity1.setTransactionId(3L);
        transactionEntity.setStatus(Status.ACCEPTED);
        transactionEntity.setTransactionDate(date);

        transactionEntities.add(transactionEntity);
        transactionEntities.add(transactionEntity1);
        transactionEntities.add(transactionEntity2);

        excepted.add(transactionEntity);
        excepted.add(transactionEntity1);

        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(1L);
        userEntity.setTransactionEntitySet(transactionEntities);

        List<TransactionEntity> list = new LinkedList<>();
        list.addAll(excepted);
        given(transactionRepo.getAllByUserEntityAndTransactionDateAndStatus(userEntity, date, Status.PENDING))
                .willReturn(list);

        List<TransactionEntity> excepted1 = transactionService.getTransactionsByFilter(userEntity, date, Status.PENDING);

        Assertions.assertEquals(excepted1, list);
    }

    @Test
    public void update_test() {
        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setStatus(Status.ACCEPTED);
        transactionEntity.setTransactionDate(LocalDate.now());
        transactionEntity.setType(Type.DEPOSIT);
        transactionEntity.setTransactionId(1L);
        given(transactionRepo.save(transactionEntity)).willReturn(transactionEntity);
        TransactionEntity transactionEntity1 = transactionService.update(transactionEntity);
        Assume.assumeNotNull(transactionEntity1);
        verify(transactionRepo).save(any(TransactionEntity.class));
    }

}

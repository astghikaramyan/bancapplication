package am.bdgexame.bancapplication.controller.transaction;

import am.bdgexame.bancapplication.controller.user.UserController;
import am.bdgexame.bancapplication.dao.TransactionDao;
import am.bdgexame.bancapplication.entity.AccountEntity;
import am.bdgexame.bancapplication.entity.TransactionEntity;
import am.bdgexame.bancapplication.entity.UserEntity;
import am.bdgexame.bancapplication.exceptions.BadRequestException;
import am.bdgexame.bancapplication.exceptions.ConflictException;
import am.bdgexame.bancapplication.mapper.TransactionMapper;
import am.bdgexame.bancapplication.model.Status;
import am.bdgexame.bancapplication.model.Type;
import am.bdgexame.bancapplication.service.AccountService;
import am.bdgexame.bancapplication.service.TransactionService;
import am.bdgexame.bancapplication.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    private TransactionService transactionService;
    private TransactionMapper transactionMapper;
    private UserService userService;
    private AccountService accountService;
    private static Logger logger = LoggerFactory.getLogger(TransactionController.class);


    @Autowired
    public TransactionController(AccountService accountService, UserService userService, TransactionMapper transactionMapper, TransactionService transactionService) {
        this.accountService = accountService;
        this.transactionMapper = transactionMapper;
        this.userService = userService;
        this.transactionService = transactionService;
    }


    @PatchMapping("/{transactionId}")
    public ResponseEntity<TransactionDao> updateTransaction(@RequestBody TransactionDao transactionDao, @PathVariable("transactionId") Long transactionId, @RequestParam("adminId") Long adminId) {
        if (userService.getById(adminId).isPresent()) {
            Optional<TransactionEntity> transactionEntity = transactionService.getById(transactionId);
            if (!transactionEntity.isPresent()) {
                logger.warn("Transaction with {} id does not exist.", transactionId);
                throw new BadRequestException("Transaction with such id doesn't exist");
            }
            if (transactionEntity.isPresent()) {
                if (transactionEntity.get().getStatus() == Status.ACCEPTED) {
                    logger.warn("Transaction with accepted status must not be changed.");
                    throw new ConflictException("Transactions with accepted status can not be changed.");
                }
            }
            if (transactionEntity.isPresent()) {
                if (transactionEntity.get().getStatus() == Status.CANCELED) {
                    logger.warn("Transaction with canceled status must not be changed.");
                    throw new ConflictException("Transactions with canceled status can not be changed.");
                }
            }
            TransactionEntity transactionEntity1 = this.transactionMapper.toEntity(transactionDao);
            transactionEntity1.setTransactionId(transactionId);
            if (transactionEntity1.getStatus() == Status.ACCEPTED) {
                transactionEntity1.setAccountEntity(transactionEntity.get().getAccountEntity());
                transactionEntity1.setUserEntity(transactionEntity.get().getUserEntity());
                AccountEntity accountEntity = transactionEntity1.getAccountEntity();
                if (transactionEntity1.getType() == Type.DEPOSIT) {
                    accountEntity.setBalance(accountEntity.getBalance() + transactionEntity1.getAmmount());
                    transactionEntity1.setAccountEntity(accountEntity);
                }
                if (transactionEntity1.getType() == Type.WITHDRAWAL) {
                    if (accountEntity.getBalance() >= transactionEntity1.getAmmount()) {
                        accountEntity.setBalance(accountEntity.getBalance() - transactionEntity1.getAmmount());
                        transactionEntity1.setAccountEntity(accountEntity);
                    } else {
                        logger.warn("Can not take money grater then your account balance.");
                        throw new ConflictException("You can not take ammount grater then in your balance.");
                    }
                }
            }
            transactionEntity1 = transactionService.update(transactionEntity1);
            return ResponseEntity.ok(transactionMapper.toDto(transactionEntity1));
        } else {
            logger.warn("Manage transactions with status accepted can only users of admin  role.");
            throw new ConflictException("Manage transactions with status accepted only can users with admin role.");
        }
    }

    @GetMapping("/filter")
    public ResponseEntity<List<TransactionDao>> getTransactionsByGivenFilter(@RequestParam(name = "userId") Long userId, @RequestParam(name = "date") String date, @RequestParam(name = "status") String status) {
        Optional<UserEntity> userEntity = userService.getById(userId);
        LocalDate ld = LocalDate.parse(date);
        if (!userEntity.isPresent()) {
            logger.warn("User with {} id does not exist.", userId);
            throw new BadRequestException("User with such id doesn't exist");
        }
        List<TransactionEntity> transactionEntities = transactionService.getTransactionsByFilter(userEntity.get(), ld, Status.valueOf(status));
        if (transactionEntities.size() == 0) {
            logger.warn("Transactions with {} user id, {} date and {} status  do not exist.", userId, ld, Status.valueOf(status));
            throw new ConflictException("Transactions with such parameters does not exist");
        }
        return ResponseEntity.ok(transactionMapper.toDtoList(transactionEntities));
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> createTransaction(@RequestBody TransactionDao transactionDao,
                                               @RequestParam(name = "userId") Long userId, @RequestParam(name = "accountId") Long accountId) {
        TransactionEntity transactionEntity = transactionMapper.toEntity(transactionDao);
        transactionEntity.setTransactionDate(LocalDate.now());
        Optional<AccountEntity> accountEntity = accountService.getById(accountId);
        Optional<UserEntity> userEntity = userService.getById(userId);
        AccountEntity accountEntity1 = new AccountEntity();
        UserEntity userEntity1 = new UserEntity();
        if (!accountEntity.isPresent()) {
            logger.warn("Account with {} id does not exist.", accountId);
            throw new BadRequestException("Account with such id does not exists");
        }
        if (!userEntity.isPresent()) {
            logger.warn("User with {} id does not exist.", userId);
            throw new BadRequestException("User with such id does not exists");
        }
        userEntity1 = userEntity.get();
        accountEntity1 = accountEntity.get();
        if (transactionEntity.getStatus() == Status.ACCEPTED && transactionEntity.getType() == Type.DEPOSIT) {
            accountEntity1.setBalance(accountEntity1.getBalance() + transactionEntity.getAmmount());
        }
        if (transactionEntity.getStatus() == Status.ACCEPTED && transactionEntity.getType() == Type.WITHDRAWAL) {
            if (accountEntity1.getBalance() >= transactionEntity.getAmmount())
                accountEntity1.setBalance(accountEntity1.getBalance() - transactionEntity.getAmmount());
        }
        transactionEntity.setUserEntity(userEntity1);
        transactionEntity.setAccountEntity(accountEntity1);
        transactionEntity = transactionService.save(transactionEntity);
        Set<TransactionEntity> transactionEntitySet = new HashSet<>();
        if (userEntity1.getTransactionEntitySet().size() != 0) {
            transactionEntitySet.addAll(userEntity1.getTransactionEntitySet());
            userEntity1.getTransactionEntitySet().clear();
            transactionEntitySet.add(transactionEntity);
            userEntity1.getTransactionEntitySet().addAll(transactionEntitySet);
        } else {
            userEntity1.getTransactionEntitySet().clear();
            transactionEntitySet.add(transactionEntity);
            userEntity1.getTransactionEntitySet().addAll(transactionEntitySet);
        }
        Set<TransactionEntity> transactionEntities = new HashSet<>();
        if (accountEntity1.getTransactionEntitySet().size() != 0) {
            transactionEntities.addAll(accountEntity1.getTransactionEntitySet());
            accountEntity1.getTransactionEntitySet().clear();
            transactionEntities.add(transactionEntity);
            accountEntity1.getTransactionEntitySet().addAll(transactionEntities);
        } else {
            accountEntity1.getTransactionEntitySet().clear();
            transactionEntities.add(transactionEntity);
            accountEntity1.getTransactionEntitySet().addAll(transactionEntitySet);
        }
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<?> cancelTransaction(@PathVariable(name = "id") Long id) {
        Optional<TransactionEntity> transactionEntity = transactionService.getById(id);
        if (transactionEntity.isPresent()) {
            if (transactionEntity.get().getStatus() == Status.CANCELED) {
                throw new ConflictException("Transaction with such id already canceled.");
            }
            TransactionEntity transactionEntity1 = transactionEntity.get();
            if (transactionEntity1.getStatus() == Status.PENDING) {
                transactionEntity1.setStatus(Status.CANCELED);
                transactionService.update(transactionEntity1);
            } else {
                logger.warn("Transaction with not Pending status is not canceled.");
                throw new ConflictException("Transaction with not Pending status is not canceled.");
            }
        } else {
            logger.warn("Transaction with {} id does not exist.", id);
            throw new ConflictException("Transaction with such id does not exist.");
        }
        return ResponseEntity.ok().build();
    }
}

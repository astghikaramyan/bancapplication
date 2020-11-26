package am.bdgexame.bancapplication.controller.account;

import am.bdgexame.bancapplication.controller.user.UserController;
import am.bdgexame.bancapplication.dao.AccountDao;
import am.bdgexame.bancapplication.entity.AccountEntity;
import am.bdgexame.bancapplication.entity.UserEntity;
import am.bdgexame.bancapplication.exceptions.BadRequestException;
import am.bdgexame.bancapplication.exceptions.ConflictException;
import am.bdgexame.bancapplication.mapper.AccountMapper;
import am.bdgexame.bancapplication.service.AccountService;
import am.bdgexame.bancapplication.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    private AccountService accountService;
    private UserService userService;
    private AccountMapper accountMapper;
    private static Logger logger = LoggerFactory.getLogger(AccountController.class);


    @Autowired
    public AccountController(AccountService accountService, AccountMapper accountMapper, UserService userService) {
        this.accountService = accountService;
        this.accountMapper = accountMapper;
        this.userService = userService;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<AccountDao> createAccount(@RequestBody @Valid AccountDao accountDao,
                                                    @RequestParam(name = "userId") Long userId) {
        AccountEntity accountEntity = this.accountMapper.toEntity(accountDao);
        AccountEntity accountEntity1 = accountService.findAccountByAccessCodeAndAccountNumber(accountEntity.getAccessCode(), accountEntity.getAccountNumber());
        if (accountEntity1 != null) {
            logger.warn("Account with {} account number and {} access code already exists.",
                    accountDao.getAccountNumber(), accountDao.getAccessCode());
            throw new ConflictException("Account with such account number and access code already exists.");
        }
        Optional<UserEntity> userEntity = userService.getById(userId);
        if (!userEntity.isPresent()) {
            logger.warn("User with {} id does not exist.", userId);
            throw new BadRequestException("User with such id does not exists.");
        }
        accountEntity.setUserEntity(userEntity.get());
        accountEntity.setCreatedDate(LocalDate.now());
        accountEntity = accountService.save(accountEntity);

        Set<AccountEntity> accountEntities1 = new HashSet<>();
        accountEntities1.addAll(userEntity.get().getAccountEntitySet());
        accountEntities1.add(accountEntity);
        userEntity.get().getAccountEntitySet().clear();
        userEntity.get().getAccountEntitySet().addAll(accountEntities1);
        return ResponseEntity.ok(accountMapper.toDto(accountEntity));
//        https://codippa.com/how-to-resolve-a-collection-with-cascadeall-delete-orphan-was-no-longer-referenced-by-the-owning-entity-instance/
    }

    @GetMapping("/_search")
    public ResponseEntity<AccountDao> getAccount(@RequestParam Long userId, @RequestParam Long accountId) {
        Optional<UserEntity> userEntity = userService.getById(userId);
        if (userEntity.isPresent()) {
            AccountEntity accountEntity = accountService.getAccountByAccountIdAndUser(accountId, userEntity.get());
            if (accountEntity == null) {
                logger.warn("Account for user with {} id and {} account id does not exist.", userId, accountId);
                throw new ConflictException("Account with such account id and user id doesn't exists");
            }
            return ResponseEntity.ok(accountMapper.toDto(accountEntity));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AccountDao>> getUserAccounts(@PathVariable Long userId) {
        Optional<UserEntity> userEntity = userService.getById(userId);
        if (!userEntity.isPresent()) {
            logger.warn("User with {} id does not exist.", userId);
            throw new ConflictException("User with such user id doesn't exists");
        } else {
            List<AccountEntity> accountEntity = accountService.getUserAccounts(userEntity.get());
            if (accountEntity != null) {
                logger.info("Fetching list of accounts for user with {} id.", userId);
                return ResponseEntity.ok(accountMapper.toDtoList(accountEntity));
            }
        }
        return ResponseEntity.notFound().build();
    }

}

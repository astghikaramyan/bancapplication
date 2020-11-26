package am.bdgexame.bancapplication.controller.user;

import am.bdgexame.bancapplication.dao.TransactionDao;
import am.bdgexame.bancapplication.dao.UserDao;
import am.bdgexame.bancapplication.entity.TransactionEntity;
import am.bdgexame.bancapplication.entity.UserEntity;
import am.bdgexame.bancapplication.exceptions.BadRequestException;
import am.bdgexame.bancapplication.exceptions.ConflictException;
import am.bdgexame.bancapplication.mapper.TransactionMapper;
import am.bdgexame.bancapplication.mapper.UserMapper;
import am.bdgexame.bancapplication.model.Role;
import am.bdgexame.bancapplication.service.TransactionService;
import am.bdgexame.bancapplication.service.UserService;
import am.bdgexame.bancapplication.util.EncriptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private UserService userService;

    private UserMapper userMapper;

    private TransactionService transactionService;

    private TransactionMapper transactionMapper;

    private static Logger logger = LoggerFactory.getLogger(UserController.class);


    @Autowired
    public UserController(UserService userService, UserMapper userMapper, TransactionMapper transactionMapper, TransactionService transactionService) {
        this.userMapper = userMapper;
        this.userService = userService;
        this.transactionMapper = transactionMapper;
        this.transactionService = transactionService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> addUser(@Valid @RequestBody UserDao userDao) {
        UserEntity userEntity = this.userMapper.toEntity(userDao);
        if (userService.existsByEmail(userEntity.getEmail())) {
            logger.warn("User with {} email already exists.", userDao.getEmail());
            throw new ConflictException("Email address already taken");
        }
        if (userService.existsByPassword(userEntity.getPassword())) {
            logger.warn("User with {} password already exists.", userDao.getPassword());
            throw new ConflictException("Password already in use ");
        }
        userEntity.setPassword(EncriptionUtil.encrypt(userDao.getPassword()));
        userEntity.setRole(Role.USER);
        userEntity = this.userService.save(userEntity);
        if (userEntity != null) {
            logger.info("User was saved successfully. ");
            return ResponseEntity.ok(userMapper.toDto(userEntity));
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/signIn")
    public ResponseEntity<UserDao> loginUser(@RequestParam String email, @RequestParam String password) {
        Boolean exists = userService.existsUserByEmailAndPassword(email, password);
        if (!exists) {
            logger.warn("User with {} email and {} password does not exist.", email, password);
            throw new BadRequestException("User with such email and password doesn't exist");
        }
        UserEntity userEntity = userService.getUserByEmailAndPassword(email, password);
        return ResponseEntity.ok(userMapper.toDto(userEntity));
    }

    @PatchMapping("/{userId}/role")
    public ResponseEntity<UserDao> updateUser(@RequestBody UserDao userDao, @PathVariable Long userId, @RequestParam("adminId") Long adminId) {
        Optional<UserEntity> admin = userService.getById(adminId);
        if (admin.isPresent()) {
            if (admin.get().getRole() == Role.ADMIN) {
                Optional<UserEntity> userEntity1 = userService.getById(userId);
                if (userEntity1.isPresent()) {
                    UserEntity userEntity2 = userEntity1.get();
                    userEntity2.setRole(userDao.getRole());
                    userEntity2 = userService.updateUser(userEntity2);
                    return ResponseEntity.ok(userMapper.toDto(userEntity2));
                }else{
                    logger.warn("User with {} id does not exist.", userId);
                    throw new BadRequestException("User with such id doesn't exists.");
                }
            } else {
                logger.warn("User should have admin role.");
                throw new ConflictException("User with not admin role cannot change other user role.");
            }
        } else {
            logger.warn("Admin with {} id does not exist.", adminId);
            throw new BadRequestException("Admin with such id does not exits.");
        }
    }

    @GetMapping("/{id}/transaction")
    public ResponseEntity<List<TransactionDao>> getTransactions(@PathVariable Long id) {
        Optional<UserEntity> userEntity = userService.getById(id);
        if (!userEntity.isPresent()) {
            logger.warn("User with {} id does not exist.", id);
            throw new BadRequestException("User with such id doesn't exist");
        }
        List<TransactionEntity> transactionEntities = transactionService.getUserTransactions(userEntity.get());
        if (transactionEntities == null) {
            logger.warn("User with {} id does not have any transactions.", id);
            throw new ConflictException("User doesn't make any transaction.");
        }
        return ResponseEntity.ok(transactionMapper.toDtoList(transactionEntities));
    }

    @GetMapping("/{id}/transaction/filter")
    public ResponseEntity<List<TransactionDao>> getTransactions(@PathVariable Long id, @RequestParam String date) {
        Optional<UserEntity> userEntity = userService.getById(id);
        LocalDate ld = LocalDate.parse(date);
        if (!userEntity.isPresent()) {
            logger.warn("User with {} id does not exist.", id);
            throw new BadRequestException("User with such id doesn't exist");
        }
        List<TransactionEntity> transactionEntities = transactionService.getUserTransactionsByDate(userEntity.get(), ld);
        if (transactionEntities == null) {
            logger.warn("User with {} id does not make any transactions.", id);
            throw new ConflictException("User doesn't make any transaction.");
        }
        return ResponseEntity.ok(transactionMapper.toDtoList(transactionEntities));
    }
}


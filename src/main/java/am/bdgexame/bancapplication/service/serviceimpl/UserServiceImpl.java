package am.bdgexame.bancapplication.service.serviceimpl;

import am.bdgexame.bancapplication.entity.UserEntity;
import am.bdgexame.bancapplication.exceptions.ConflictException;
import am.bdgexame.bancapplication.repo.UserRepo;
import am.bdgexame.bancapplication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;

    @Autowired
    public UserServiceImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public Optional<UserEntity> getById(long id) {
        Optional<UserEntity> userEntity = userRepo.findById(id);
        return userEntity;
    }

    @Override
    public Set<UserEntity> getAll() {
        Set<UserEntity> userEntities = new HashSet<>();
        Iterable<UserEntity> userEntities1 = userRepo.findAll();
        if (userEntities1 != null) {
            for (UserEntity u : userEntities1) {
                userEntities.add(u);
            }
            return userEntities;
        }
        return null;
    }

    @Override
    public UserEntity save(UserEntity userEntity) {

        if (userRepo.existsByEmail(userEntity.getEmail())) {
            throw new ConflictException("User with such email exists.");
        }
        if (userEntity.getEmail().isEmpty()) {
            throw new ConflictException("Email should be provided.");
        }
        if (userEntity.getPassword().isEmpty()) {
            throw new ConflictException("Password should be provided.");
        }
        userEntity = userRepo.save(userEntity);
        return userEntity;
    }

    @Override
    public void delete(long id) {
        Optional<UserEntity> userEntity = userRepo.findById(id);
        if (userEntity.isPresent()) {
            userRepo.deleteById(id);
        }
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userRepo.existsByEmail(email);
    }

    @Override
    public Boolean existsByPassword(String password) {
        return userRepo.existsByPassword(password);
    }

    @Override
    public Boolean existsUserByEmailAndPassword(String email, String password) {
        return userRepo.existsByEmailAndPassword(email, password);
    }

    @Override
    public UserEntity getUserByEmailAndPassword(String email, String password) {
        return userRepo.findUserEntityByEmailAndPassword(email, password);
    }

    @Override
    public UserEntity updateUser(UserEntity userEntity) {
        return userRepo.save(userEntity);
    }
}

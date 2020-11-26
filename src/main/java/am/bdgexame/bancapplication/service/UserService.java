package am.bdgexame.bancapplication.service;

import am.bdgexame.bancapplication.entity.AccountEntity;
import am.bdgexame.bancapplication.entity.UserEntity;

import java.util.Optional;
import java.util.Set;

public interface UserService {
    Optional<UserEntity> getById(long id);

    Set<UserEntity> getAll();

    UserEntity save(UserEntity userEntity);

    void delete(long id);

    Boolean existsByEmail(String email);

    Boolean existsByPassword(String password);

    Boolean existsUserByEmailAndPassword(String email, String password);

    UserEntity getUserByEmailAndPassword(String email, String password);

    UserEntity updateUser(UserEntity userEntity);
}

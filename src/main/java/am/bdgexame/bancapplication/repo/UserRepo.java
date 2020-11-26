package am.bdgexame.bancapplication.repo;

import am.bdgexame.bancapplication.entity.TransactionEntity;
import am.bdgexame.bancapplication.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<UserEntity, Long> {
    Boolean existsByEmail(String email);

    Boolean existsByPassword(String password);

    Boolean existsByEmailAndPassword(String email, String password);

    UserEntity findUserEntityByEmailAndPassword(String email, String password);
}

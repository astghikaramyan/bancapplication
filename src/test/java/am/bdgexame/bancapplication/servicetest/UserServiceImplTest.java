package am.bdgexame.bancapplication.servicetest;

import am.bdgexame.bancapplication.entity.UserEntity;
import am.bdgexame.bancapplication.exceptions.ConflictException;
import am.bdgexame.bancapplication.model.Role;
import am.bdgexame.bancapplication.repo.UserRepo;
import am.bdgexame.bancapplication.service.serviceimpl.UserServiceImpl;
import org.junit.Assume;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static org.mockito.BDDMockito.*;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;


@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void when_save_user_should_has_user_role() {
        UserEntity userEntity = new UserEntity();
        userEntity.setRole(Role.USER);
        userEntity.setEmail("asas@mail.ru");
        userEntity.setPassword("Aasgdj15@");
        Mockito.when(userRepo.save(any(UserEntity.class))).thenReturn(userEntity);
        UserEntity userEntity1 = userService.save(userEntity);
        assertEquals(Role.USER, userEntity1.getRole());
    }

    @Test
    public void when_save_user_without_email_it_throw_exception() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            UserEntity userEntity = new UserEntity();
            userEntity.setRole(Role.USER);
            userEntity.setPassword("Ahusd2@");
            userService.save(userEntity);
        });
    }

    @Test
    public void when_save_user_without_password_it_throw_exception() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            UserEntity userEntity = new UserEntity();
            userEntity.setRole(Role.USER);
            userEntity.setEmail("asa@mail.ru");
            userService.save(userEntity);
        });
    }

    @Test
    public void when_save_user_it_should_be_done_successfully() {
        UserEntity userEntity = new UserEntity();
        userEntity.setRole(Role.USER);
        userEntity.setEmail("asa@mail.ru");
        userEntity.setPassword("Pasgj@ad15");
        userEntity.setRole(Role.USER);
        given(userRepo.save(userEntity)).willAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
        UserEntity userEntity1 = userService.save(userEntity);
        Assume.assumeNotNull(userEntity1);

        verify(userRepo).save(any(UserEntity.class));
    }

    @Test
    public void when_save_user_with_existing_email_it_should_throw_error() {

        Assertions.assertThrows(ConflictException.class, () -> {
            UserEntity userEntity = new UserEntity();
            userEntity.setRole(Role.USER);
            userEntity.setEmail("asa@mail.ru");
            userEntity.setPassword("Pasgj@ad15");
            userEntity.setRole(Role.USER);
            userEntity.setUserId(1L);

            given(userRepo.existsByEmail(userEntity.getEmail())).willReturn(true);
            userService.save(userEntity);
        });
        verify(userRepo, never()).save(any(UserEntity.class));
    }

    @Test
    public void update_user_test() {
        UserEntity userEntity = new UserEntity();
        userEntity.setRole(Role.USER);
        userEntity.setEmail("asa@mail.ru");
        userEntity.setPassword("Pasgj@ad15");
        userEntity.setRole(Role.USER);
        userEntity.setUserId(1L);
        given(userRepo.save(userEntity)).willReturn(userEntity);
        UserEntity userEntity1 = userService.updateUser(userEntity);
        Assume.assumeNotNull(userEntity1);
        verify(userRepo).save(any(UserEntity.class));
    }

    @Test
    public void get_all_test() {
        List<UserEntity> userEntityList = new LinkedList<>();
        UserEntity userEntity = new UserEntity(1L, "asa", "as", 20, "asa@mail.ru", "Ahs@njds", Role.USER);
        UserEntity userEntity1 = new UserEntity(2L, "asa", "as", 25,
                "adsfsa@mail.ru", "Ahdfsfs@njds", Role.USER);
        userEntityList.add(userEntity);
        userEntityList.add(userEntity1);

        given(userRepo.findAll()).willReturn(userEntityList);

        Set<UserEntity> userEntities = new HashSet<>();
        userEntities.addAll(userEntityList);

        Set<UserEntity> userEntities1 = userService.getAll();
        assertEquals(userEntities1, userEntities);

    }

    @Test
    public void get_by_id_test() {
        final Long id = 1L;
        final UserEntity userEntity = new UserEntity(1L, "a", "aa", 15, "a@mail.ru", "Ajs@", Role.USER);
        given(userRepo.findById(userEntity.getUserId())).willReturn(Optional.of(userEntity));
        final Optional<UserEntity> expected = userService.getById(id);
        Assume.assumeNotNull(expected);
    }

    @Test
    public void delete_test() {
        final Long id = 1L;

        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(id);
        given(userRepo.findById(id)).willReturn(Optional.of(userEntity));
        userService.delete(id);
        userService.delete(id);
        verify(userRepo, times(2)).deleteById(id);
    }

    @Test
    public void exist_by_email_test() {
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail("ashs@mail.ru");
        given(userRepo.existsByEmail(userEntity.getEmail())).willReturn(true);
        Boolean expected = userService.existsByEmail(userEntity.getEmail());
        assertEquals(expected, true);
    }

    @Test
    public void exist_by_password_test() {
        String password = "Ahd@12";
        given(userRepo.existsByPassword(password)).willReturn(true);
        Boolean expected = userService.existsByPassword(password);
        assertEquals(expected, true);
    }

    @Test
    public void get_user_by_email_and_password_test() {
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail("asas@mail.ru");
        userEntity.setPassword("@Ajdfsj12");
        given(userRepo.findUserEntityByEmailAndPassword(userEntity.getEmail(), userEntity.getPassword())).willReturn(userEntity);
        UserEntity excepted = userService.getUserByEmailAndPassword(userEntity.getEmail(), userEntity.getPassword());
        assertEquals(excepted, userEntity);
    }

    @Test
    public void exist_by_email_and_password_test() {
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail("asas@mail.ru");
        userEntity.setPassword("@Ajdfsj12");
        given(userRepo.existsByEmailAndPassword(userEntity.getEmail(), userEntity.getPassword())).willReturn(true);
        Boolean excepted = userService.existsUserByEmailAndPassword(userEntity.getEmail(), userEntity.getPassword());
        assertEquals(excepted, true);
    }
}
//https://medium.com/backend-habit/integrate-junit-and-mockito-unit-testing-for-service-layer-a0a5a811c58a
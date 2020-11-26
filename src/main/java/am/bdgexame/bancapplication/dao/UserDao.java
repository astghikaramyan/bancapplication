package am.bdgexame.bancapplication.dao;

import am.bdgexame.bancapplication.model.Role;
import am.bdgexame.bancapplication.validator.annotations.ValidPassword;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;
import java.util.Set;

@Data
@EqualsAndHashCode
@NoArgsConstructor
public class UserDao {

    private Long userId;

    @NotBlank(message = "Name cannot be missing or empty.")
    @Size(min = 1, message = "Name must not be less then 1 characters.")
    private String name;

    @NotNull(message = "Surname cannot be missing or empty.")
    private String surname;

    @NotBlank(message = "Age cannot be missing or empty.")
    private Integer age;

    @Email
    private String email;

    @ValidPassword
    private String password;

    private Role role;

    private Set<AccountDao> accountDaos;

    private Set<TransactionDao> transactionDaos;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserDao)) return false;
        UserDao userDao = (UserDao) o;
        return email.equals(userDao.email) &&
                password.equals(userDao.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, password);
    }

    @Override
    public String toString() {
        return "UserDao{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", age=" + age +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                '}';
    }
}

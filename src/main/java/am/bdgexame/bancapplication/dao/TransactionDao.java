package am.bdgexame.bancapplication.dao;

import am.bdgexame.bancapplication.model.Status;
import am.bdgexame.bancapplication.model.Type;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@EqualsAndHashCode
@NoArgsConstructor
public class TransactionDao {

    private Long transactionId;

    @NotNull(message = "Fill filed of type.")
    private Type type;

    @NotNull(message = "Fill filed of ammount.")
    private Long ammount;

    private LocalDate transactionDate;

    @NotNull(message = "Fill filed of status one of the following: PENDING or ACCEPTED.")
    private Status status;

    private UserDao userDao;

    private AccountDao accountDao;

    @Override
    public String toString() {
        return "TransactionDao{" +
                "transactionId=" + transactionId +
                ", type=" + type +
                ", ammount=" + ammount +
                ", transactionDate=" + transactionDate +
                ", status=" + status +
                '}';
    }
}

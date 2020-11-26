package am.bdgexame.bancapplication.dao;

import am.bdgexame.bancapplication.model.Currency;
import am.bdgexame.bancapplication.model.Type;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

@Data
@EqualsAndHashCode
@NoArgsConstructor
public class AccountDao {

    private Long accountId;

    @NotNull(message = "Account number cannot be missing or empty.")
    private Long accountNumber;

    @NotNull(message = "Access code cannot be missing or empty.")
    private Long accessCode;

    @NotNull(message = "Field of account type cannot be missing or empty.")
    private Type accountType;

    @NotNull(message = "Fill filed of currency.")
    private Currency currency;

    @NotNull(message = "Fill filed of balance.")
    private Long balance;

    private LocalDate createdDate;

    UserDao userDao;

    Set<TransactionDao> transactionDaos;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccountDao)) return false;
        AccountDao that = (AccountDao) o;
        return accountNumber.equals(that.accountNumber) &&
                accessCode.equals(that.accessCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNumber, accessCode);
    }

    @Override
    public String toString() {
        return "AccountDao{" +
                "accountId=" + accountId +
                ", accountNumber=" + accountNumber +
                ", accessCode=" + accessCode +
                ", accountType=" + accountType +
                ", currency=" + currency +
                ", balance=" + balance +
                ", createdDate=" + createdDate +
                '}';
    }
}

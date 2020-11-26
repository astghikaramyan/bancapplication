package am.bdgexame.bancapplication.entity;

import am.bdgexame.bancapplication.model.Currency;
import am.bdgexame.bancapplication.model.Type;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "accounts")
@Data
@EqualsAndHashCode
public class AccountEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "account_number")
    private Long accountNumber;

    @Column(name = "access_code")
    private Long accessCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type")
    private Type accountType;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    private Long balance;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    UserEntity userEntity;

    @OneToMany(
            mappedBy = "accountEntity",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            orphanRemoval = true
    )
    @Fetch(FetchMode.SELECT)
    Set<TransactionEntity> transactionEntitySet;

    public AccountEntity(Long accountNumber, Long accessCode) {
        this.accountNumber = accountNumber;
        this.accessCode = accessCode;
    }

    public AccountEntity() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccountEntity)) return false;
        AccountEntity that = (AccountEntity) o;
        return accountNumber.equals(that.accountNumber) &&
                accessCode.equals(that.accessCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNumber, accessCode);
    }

    @Override
    public String toString() {
        return "AccountEntity{" +
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

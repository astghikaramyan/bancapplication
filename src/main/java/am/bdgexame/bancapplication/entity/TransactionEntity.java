package am.bdgexame.bancapplication.entity;

import am.bdgexame.bancapplication.model.Status;
import am.bdgexame.bancapplication.model.Type;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "transactions")
@Data
public class TransactionEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Long transactionId;

    @Enumerated(EnumType.STRING)
    private Type type;

    private Long ammount;

    @Column(name = "transaction_date")
    private LocalDate transactionDate;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id")
    private AccountEntity accountEntity;

    @Override
    public String toString() {
        return "TransactionEntity{" +
                "transactionId=" + transactionId +
                ", type=" + type +
                ", ammount=" + ammount +
                ", transactionDate=" + transactionDate +
                ", status=" + status +
                '}';
    }
}

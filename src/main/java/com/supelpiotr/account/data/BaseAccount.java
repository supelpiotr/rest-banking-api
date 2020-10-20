package com.supelpiotr.account.data;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.UUID;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class BaseAccount implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @GeneratedValue
    private String accountNumber;

    @Enumerated(EnumType.STRING)
    private AccountType type = AccountType.PLN;

    @Column
    private BigDecimal balance = BigDecimal.valueOf(0);

    @PrePersist
    private void ensureId(){
        this.setAccountNumber(String.format("%040d", new BigInteger(UUID.randomUUID().toString().replace("-", ""), 16)));
    }

}

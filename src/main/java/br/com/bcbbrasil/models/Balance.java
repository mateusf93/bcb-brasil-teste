package br.com.bcbbrasil.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "balance")
@Getter
@Setter
public class Balance {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column
    private BigDecimal totalBalance;
    @Column
    private BigDecimal currentBalance;
    @Column
    private BigDecimal totalSpent;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    private BalanceType balanceType;

}

package com.example.java_study_part_5.models;

import com.example.java_study_part_5.models.dicts.AccountPool;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "number")
    private String number;

    @OneToOne(mappedBy = "account")
    private Register register;

    @Column(name = "currency_iso_code")
    private String currencyIsoCode;

    @Column(name = "state")
    private String state;

    @ManyToOne
    @JoinColumn(name = "account_pool_id")
    private AccountPool accountPool;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}

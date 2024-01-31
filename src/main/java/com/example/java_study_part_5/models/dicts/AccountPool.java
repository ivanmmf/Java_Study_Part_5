package com.example.java_study_part_5.models.dicts;


import com.example.java_study_part_5.models.Account;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Table(name = "account_pool")
public class AccountPool {
    @Id
    private Long id;

    @Column(name = "branch_code")
    private String branchCode;

    @Column(name = "currency_code")
    private String currencyCode;

    @Column(name = "mdm_code")
    private String mdmCode;

    @Column(name = "priority_code")
    private String priorityCode;

    @Column(name = "registry_type_code")
    private String registryTypeCode;


    @OneToMany(mappedBy = "accountPool")
    private List<Account> accounts;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}

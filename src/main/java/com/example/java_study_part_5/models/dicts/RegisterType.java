package com.example.java_study_part_5.models.dicts;


import com.example.java_study_part_5.enums.AccountType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "product_register_type")
public class RegisterType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "value1")
    private String value;

    @Column(name = "register_type_code", unique = true, nullable = false)
    private String registerTypeCode;

    @Column(name = "register_type_name")
    private String registerTypeName;
    @ManyToOne
    @JoinColumn(name = "product_class_id")
    private ProductClass productClass;

    @Column(name = "account_type")
//    @Enumerated(EnumType.STRING)
//  //  @Convert(converter = AccountType.class)
    private String accountType;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}

package com.example.java_study_part_5.models;

import com.example.java_study_part_5.models.dicts.RegisterType;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_code_id")
    private Long productCodeId;

    @Column(name = "client_id")
    private Long clientId;

   @ManyToOne
   @JoinColumn(name = "register_type_id")
    private RegisterType type;

    @Column(name = "number")
    private String number;

    @Column(name = "priority")
    private String priority;

    @Column(name = "date_of_conclusion")
    private String dateOfConclusion;

    @Column(name = "start_date_time")
    private String startDateTime;

    @Column(name = "end_date_time")
    private String endDateTime;
    @Column(name = "days")
    private int days;

    @Column(name = "penalty_rate")
    private  float penaltyRate;

    @Column(name = "nso")
    private  float nso;

    @Column(name = "threshold_amount")
    private  float thresholdAmount;

    @Column(name = "requisite_type")
    private  String requisiteType;

    @Column(name = "interest_rate_type")
    private String interestRateType;

    @Column(name = "tax_rate")
    private float taxRate;

    @Column(name = "reason_close")
    private  String reasonClose;

    @Column(name = "state")
    private String state;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Register> registers;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Agreement> agreements;



    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}

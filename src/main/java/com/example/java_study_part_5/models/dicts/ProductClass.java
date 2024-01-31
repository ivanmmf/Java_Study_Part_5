package com.example.java_study_part_5.models.dicts;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Table(name = "product_class")
public class ProductClass {

    @Id
    private Long id;
    @Column(name = "value1")
    private String value;

    @Column(name = "gbl_code")
    private String gblCode;

    @Column(name = "gbl_name")
    private String gblName;

    @Column(name = "product_row_code")
    private String productRowCode;

    @Column(name = "product_row_name")
    private String productRowName;

    @Column(name = "subclass_code")
    private String subclass_code;

    @Column(name = "subclass_name")
    private String subclass_name;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}

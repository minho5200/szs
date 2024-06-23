package com.szs.prj.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Builder
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CreditCardDeduction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_taxation_info_id", nullable = false)
    @Setter
    private UserTaxationInfo userTaxationInfo;

    @Column(name="deduction_year", nullable = false)
    private int year; // 연도

}

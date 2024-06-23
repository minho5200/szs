package com.szs.prj.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CreditCardDeductionMonth {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "credit_card_deduction_id", nullable = false)
    private CreditCardDeduction creditCardDeduction;

    @Column(name= "credit_card_deduction_month",nullable = false)
    private String month; // 월

    @Column(nullable = false)
    private String amount; // 공제액
}

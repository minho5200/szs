package com.szs.prj.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Pension {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_taxation_info_id", nullable = false)
    @Setter
    private UserTaxationInfo userTaxationInfo;

    @Column(name = "pension_month", nullable = false)
    private String month; // 월

    @Column(nullable = false)
    private String deductionAmount; // 공제액

}

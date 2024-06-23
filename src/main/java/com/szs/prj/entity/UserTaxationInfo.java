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
public class UserTaxationInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String userId;

    @Column(name= "taxation_year", nullable = false)
    private int taxationYear;

    @Column(nullable = false)
    private Long totalIncome; // 종합소득금액

    @Column(nullable = false)
    private String name; // 이름

    @Column(nullable = false)
    private String taxDeduction; // 세액공제
}

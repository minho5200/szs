package com.szs.prj.repository;

import com.szs.prj.entity.CreditCardDeduction;
import com.szs.prj.entity.UserTaxationInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CreditCardDeductionRepository extends JpaRepository<CreditCardDeduction, Long> {
    Optional<CreditCardDeduction> findByUserTaxationInfoId(Long userTaxationInfoId);
}


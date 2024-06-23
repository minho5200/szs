package com.szs.prj.repository;

import com.szs.prj.entity.CreditCardDeductionMonth;
import com.szs.prj.entity.UserTaxationInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CreditCardDeductionMonthRepository extends JpaRepository<CreditCardDeductionMonth, Long> {
    List<CreditCardDeductionMonth> findByCreditCardDeductionId(Long creditCardDeductionId);
}
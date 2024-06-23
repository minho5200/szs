package com.szs.prj.repository;

import com.szs.prj.entity.Pension;
import com.szs.prj.entity.UserTaxationInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PensionRepository extends JpaRepository<Pension, Long> {
    List<Pension> findByUserTaxationInfoId(Long userTaxationInfoId);
}

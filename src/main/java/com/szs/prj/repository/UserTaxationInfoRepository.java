package com.szs.prj.repository;

import com.szs.prj.entity.UserTaxationInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserTaxationInfoRepository extends JpaRepository<UserTaxationInfo, Long> {
    Optional<UserTaxationInfo> findByUserId(String userId);

    Optional<UserTaxationInfo> findByUserIdAndTaxationYear(String userId, int taxationYear);
}

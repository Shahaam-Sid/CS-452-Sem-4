package com.shahaam.equipend.repositories;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.shahaam.equipend.models.Settlement;

public interface SettlementsRepository extends JpaRepository<Settlement, UUID> {

    long countByGroup_GroupID(UUID groupID);

    List<Settlement> findByGroup_GroupID(UUID groupID);

    List<Settlement> findByPaidTo_UserID(UUID userID);

    List<Settlement> findByPaidBy_UserID(UUID userID);

    List<Settlement> findByGroup_GroupIDAndPaidTo_UserID(UUID groupID, UUID userID);

    List<Settlement> findByGroup_GroupIDAndPaidBy_UserID(UUID groupID, UUID userID);

    @Query(
        value = "SELECT COALESCE(SUM(amount), 0) FROM settlement WHERE paid_by = :paidBy AND paid_to = :paidTo",
        nativeQuery = true
    )
    BigDecimal allRecordedSettlementBetween(@Param("paidBy") UUID paidBy, @Param("paidTo") UUID paidTo);
}

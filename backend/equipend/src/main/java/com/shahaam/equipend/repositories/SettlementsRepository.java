package com.shahaam.equipend.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shahaam.equipend.models.Settlement;

public interface SettlementsRepository extends JpaRepository<Settlement, UUID> {

    long countByGroup_GroupID(UUID groupID);

    List<Settlement> findByGroup_GroupID(UUID groupID);

    List<Settlement> findByPaidto_UserID(UUID userID);

    List<Settlement> findByPaidBy_UserID(UUID userID);

    List<Settlement> findByGroup_GroupIDAndPaidTo_UserID(UUID groupID, UUID userID);

    List<Settlement> findByGroup_GroupIDAndPaidBy_UserID(UUID groupID, UUID userID);
}

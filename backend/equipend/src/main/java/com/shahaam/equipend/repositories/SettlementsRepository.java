package com.shahaam.equipend.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shahaam.equipend.models.Settlement;

public interface SettlementsRepository extends JpaRepository<Settlement, UUID> {

}

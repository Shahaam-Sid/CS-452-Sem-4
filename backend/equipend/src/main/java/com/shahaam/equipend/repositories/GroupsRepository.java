package com.shahaam.equipend.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shahaam.equipend.models.Group;

public interface GroupsRepository extends JpaRepository<Group, UUID>  {

}

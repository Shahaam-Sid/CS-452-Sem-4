package com.shahaam.equipend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shahaam.equipend.models.ExpenseSplit;
import com.shahaam.equipend.models.ExpenseSplitID;

public interface ExpenseSplitsRepository extends JpaRepository<ExpenseSplit, ExpenseSplitID> {

}

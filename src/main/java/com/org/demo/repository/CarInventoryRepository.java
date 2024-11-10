package com.org.demo.repository;

import com.org.demo.entity.CarInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarInventoryRepository extends JpaRepository<CarInventory, String> {

    Optional<CarInventory> findByCarType(String carType);
}

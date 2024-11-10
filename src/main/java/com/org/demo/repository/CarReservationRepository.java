package com.org.demo.repository;

import com.org.demo.entity.CarReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarReservationRepository  extends JpaRepository<CarReservation, Long> {
}

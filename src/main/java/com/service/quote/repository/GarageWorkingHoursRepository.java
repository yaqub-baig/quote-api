package com.service.quote.repository;

import java.time.DayOfWeek;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.service.quote.entity.GarageWorkingHours;

public interface GarageWorkingHoursRepository extends JpaRepository<GarageWorkingHours, Integer> {
	Optional<GarageWorkingHours> findByGarageIdAndDayOfWeek(Integer garageId, DayOfWeek dayOfWeek);
}
	
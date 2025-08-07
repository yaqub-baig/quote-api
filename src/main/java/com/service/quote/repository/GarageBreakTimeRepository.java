package com.service.quote.repository;

import java.time.DayOfWeek;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.service.quote.entity.GarageBreakTime;

public interface GarageBreakTimeRepository extends JpaRepository<GarageBreakTime, Integer> {
	List<GarageBreakTime> findByGarageIdAndDayOfWeek(Integer garageId, DayOfWeek dayOfWeek);
}
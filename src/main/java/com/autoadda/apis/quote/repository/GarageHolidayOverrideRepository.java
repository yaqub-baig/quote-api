package com.autoadda.apis.quote.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.autoadda.apis.quote.entity.GarageHolidayOverride;

public interface GarageHolidayOverrideRepository extends JpaRepository<GarageHolidayOverride, Integer> {
    Optional<GarageHolidayOverride> findByGarageIdAndDate(Integer garageId, LocalDate date);
}
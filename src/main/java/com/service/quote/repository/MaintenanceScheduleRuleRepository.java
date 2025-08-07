package com.service.quote.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.service.quote.entity.MaintenanceScheduleRule;

public interface MaintenanceScheduleRuleRepository extends JpaRepository<MaintenanceScheduleRule, Integer> {
    Optional<MaintenanceScheduleRule> findByMakeAndModelAndIsActiveTrue(String make, String model);
}
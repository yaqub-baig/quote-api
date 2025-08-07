package com.service.quote.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.service.quote.entity.FutureAppointment;
import com.service.quote.util.Status;

public interface FutureAppointmentRepository extends JpaRepository<FutureAppointment, Integer> {
	boolean existsByVehicleIdAndServiceTypeAndStatus(Integer vehicleId, String serviceType, Status status);
}
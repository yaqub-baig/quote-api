package com.autoadda.apis.quote.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.autoadda.apis.quote.entity.FutureAppointment;
import com.autoadda.apis.quote.util.Status;

public interface FutureAppointmentRepository extends JpaRepository<FutureAppointment, Integer> {
	boolean existsByVehicleIdAndServiceTypeAndStatus(Integer vehicleId, String serviceType, Status status);
}
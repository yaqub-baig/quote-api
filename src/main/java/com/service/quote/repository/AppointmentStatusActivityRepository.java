package com.service.quote.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.service.quote.entity.AppointmentStatusActivity;

@Repository
public interface AppointmentStatusActivityRepository extends JpaRepository<AppointmentStatusActivity, Integer>{

}

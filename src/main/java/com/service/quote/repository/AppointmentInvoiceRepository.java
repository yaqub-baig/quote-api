package com.service.quote.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.service.quote.entity.AppointmentInvoice;

@Repository
public interface AppointmentInvoiceRepository extends JpaRepository<AppointmentInvoice, Integer>{

	AppointmentInvoice findByAppointmentId(Integer appointmentId);

}

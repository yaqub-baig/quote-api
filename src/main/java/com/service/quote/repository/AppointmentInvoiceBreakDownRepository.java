package com.service.quote.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.service.quote.entity.AppointmentInvoiceBreakDown;

@Repository
public interface AppointmentInvoiceBreakDownRepository extends JpaRepository<AppointmentInvoiceBreakDown, Integer>{

}

package com.service.quote.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.service.quote.entity.GarageLicenseDetails;

@Repository
public interface GarageLicenseDetailsRepository extends JpaRepository<GarageLicenseDetails, Integer> {

	GarageLicenseDetails findByGarageId(Integer garageId);

}

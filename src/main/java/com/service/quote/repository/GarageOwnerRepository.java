package com.service.quote.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.service.quote.entity.GarageOwner;

@Repository
public interface GarageOwnerRepository extends JpaRepository<GarageOwner, Integer> {

//	List<GarageOwner> findByGarageId(Integer garageId);
}

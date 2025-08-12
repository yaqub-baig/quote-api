package com.autoadda.apis.quote.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.autoadda.apis.quote.entity.LoyaltyPoint;

public interface LoyaltyPointRepository extends JpaRepository<LoyaltyPoint, Integer> {
	Optional<LoyaltyPoint> findByOwnerIdAndGarageId(Integer ownerId, Integer garageId);

	Optional<LoyaltyPoint> findByOwnerId(Integer ownerId);

	Optional<LoyaltyPoint> findByGarageId(Integer garageId);

	Optional<LoyaltyPoint> findByOwnerIdAndGarageIdAndPoints(Integer ownerId, Integer garageId, int points);
}
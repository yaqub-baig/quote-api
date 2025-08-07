package com.service.quote.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.service.quote.entity.Referral;

public interface ReferralRepository extends JpaRepository<Referral, Integer> {
	Optional<Referral> findByReferralCode(String code);
}

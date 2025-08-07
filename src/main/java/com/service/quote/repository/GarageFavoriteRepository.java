package com.service.quote.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.service.quote.entity.GarageFavorite;

public interface GarageFavoriteRepository extends JpaRepository<GarageFavorite, Integer> {
	List<GarageFavorite> findByGarageOwnerId(Integer garageOwnerId);

	boolean existsByGarageOwnerIdAndFavoriteGarageId(Integer ownerId, Integer favoriteId);
}
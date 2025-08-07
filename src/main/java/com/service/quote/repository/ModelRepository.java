/**
 * 
 */
package com.service.quote.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.service.quote.entity.Model;

@Repository
public interface ModelRepository extends JpaRepository<Model, Integer> {
}

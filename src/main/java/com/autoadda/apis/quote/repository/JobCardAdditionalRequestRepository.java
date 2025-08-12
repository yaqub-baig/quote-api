package com.autoadda.apis.quote.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.autoadda.apis.quote.entity.JobCardAdditionalRequest;

@Repository
public interface JobCardAdditionalRequestRepository extends JpaRepository<JobCardAdditionalRequest, Integer>{


}

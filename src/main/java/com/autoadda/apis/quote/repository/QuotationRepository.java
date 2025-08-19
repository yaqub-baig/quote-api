package com.autoadda.apis.quote.repository;


import com.autoadda.apis.quote.entity.Quotation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuotationRepository extends JpaRepository<Quotation, String> {
    Optional<Quotation> findByQuoteNumber(String quoteNumber);
    @Query("SELECT r.quoteNumber FROM Quotation r WHERE r.quoteNumber LIKE :prefix% ORDER BY r.quoteNumber DESC")
    List<String> findLatestCodesByPrefix(@Param("prefix") String codePrefix);

}
package com.example.demo.currency;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyRequestRepository extends JpaRepository<CurrencyRequest, Long> {}

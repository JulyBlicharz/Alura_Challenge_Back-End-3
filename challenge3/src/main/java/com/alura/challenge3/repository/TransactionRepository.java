package com.alura.challenge3.repository;

import com.alura.challenge3.model.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface TransactionRepository extends MongoRepository<Transaction, String> {

    Transaction findFirstByDateTimeBetween(LocalDateTime startDay, LocalDateTime endDay);
}

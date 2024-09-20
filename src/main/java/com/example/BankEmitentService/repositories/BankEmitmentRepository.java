/**
 * <==================================>
 * Copyright (c) 2024 Ilya Sukhina.*
 * <=================================>
 */

package com.example.BankEmitentService.repositories;

import com.example.BankEmitentService.entity.BankEmitment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * Repository interface for managing BankEmitment entities. This interface
 * extends JpaRepository to provide basic CRUD operations on the BankEmitment
 * table and defines custom methods for additional operations.
 */
@Repository
public interface BankEmitmentRepository extends JpaRepository<BankEmitment, Integer> {

    /**
     * Truncates the entire bank_emitments table.
     * This method is used when there is a need to clear all records in the table.
     * <p>
     * The @Modifying annotation indicates that the query will modify data,
     * and the @Transactional annotation ensures that the operation occurs within
     * a transaction, allowing for rollback if necessary.
     */
    @Modifying
    @Transactional
    @Query(value = "TRUNCATE TABLE bank_emitments", nativeQuery = true)
    void truncateTable();

    /**
     * Finds and retrieves a list of BankEmitment records by the BIN (Bank Identification Number).
     * The method will return all records that match the given BIN.
     *
     * @param bin The first six digits of the card number (BIN) to search for.
     *
     * @return List of BankEmitment entities matching the BIN.
     */
    List<BankEmitment> findByBin(int bin);

}

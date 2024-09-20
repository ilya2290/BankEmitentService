/**
 * <==================================>
 * Copyright (c) 2024 Ilya Sukhina.*
 * <=================================>
 */

package com.example.BankEmitentService.services;

import com.example.BankEmitentService.repositories.BankEmitmentRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import static com.example.BankEmitentService.utils.ParseUtils.bankEmitments;
import static com.example.BankEmitentService.utils.UnzipUtils.downloadAndUnzip;

/**
 * This service class is responsible for updating bank emitment data
 * in the database using scheduled tasks. It periodically downloads
 * updated data via a cron expression and truncates the existing
 * records before saving the new entries.
 * <p>
 * The update process is executed hourly, ensuring that the database
 * contains the most recent bank emitment information.
 */
@Service
public class BankEmitmentDataUpdater {

    private static final Logger logger = LoggerFactory.getLogger(BankEmitmentDataUpdater.class);
    private final BankEmitmentRepository bankEmitmentRepository;

    /**
     * Constructs a new instance of BankEmitmentDataUpdater.
     *
     * @param bankEmitmentRepository The BankEmitmentRepository repository
     */
    @Autowired
    BankEmitmentDataUpdater(BankEmitmentRepository bankEmitmentRepository) {
        this.bankEmitmentRepository = bankEmitmentRepository;
    }

    /**
     * Scheduled method that runs every hour to update bank emitment data.
     * <p>
     * This method performs the following actions:
     * 1. Initiates a transaction.
     * 2. Downloads and unzips the latest bank emitment data.
     * 3. Truncates the existing bank emitment records in the database.
     * 4. Saves the new bank emitment records retrieved from the updated data.
     * 5. Logs the completion of the transaction.
     */
    @Scheduled(cron = "0 0 * * * *")
    @Transactional
    public void saveBankEmitment() {
        logger.info("Transaction started.");
        downloadAndUnzip();

        this.bankEmitmentRepository.truncateTable();

        logger.info("Table successfully truncated.");

//        for (BankEmitment emitment : bankEmitments()) {
//            this.bankEmitmentRepository.save(emitment);
//        }

        this.bankEmitmentRepository.saveAll(bankEmitments());

        logger.info("Transaction ended.");
    }

}

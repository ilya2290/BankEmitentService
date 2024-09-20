/**
 * <==================================>
 * Copyright (c) 2024 Ilya Sukhina.*
 * <=================================>
 */

package com.example.BankEmitentService.services;

import com.example.BankEmitentService.repositories.BankEmitmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import static com.example.BankEmitentService.utils.ParseUtils.bankEmitments;
import static com.example.BankEmitentService.utils.ZipFileUtils.downloadAndUnzip;

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
     * 1. Initiates the update process by logging the start of the transaction.
     * 2. Downloads and unzips the latest bank emitment data from the specified source.
     * 3. Truncates the existing bank emitment records in the database to prepare for new data.
     * 4. Saves the new bank emitment records retrieved from the updated data into the database.
     * 5. Logs the successful truncation of the table and the completion of the transaction.
     * </p>
     */
    @Scheduled(cron = "0 0 * * * *")
    public void saveBankEmitment() {
        logger.info("Transaction started.");
        downloadAndUnzip();

        this.bankEmitmentRepository.truncateTable();

        logger.info("Table successfully truncated.");

        this.bankEmitmentRepository.saveAll(bankEmitments());

        logger.info("Transaction ended.");
    }

}

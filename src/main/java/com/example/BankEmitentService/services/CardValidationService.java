/**
 * <==================================>
 * Copyright (c) 2024 Ilya Sukhina.*
 * <=================================>
 */

package com.example.BankEmitentService.services;

import com.example.BankEmitentService.dto.BankEmitmentDTO;
import com.example.BankEmitentService.entity.BankEmitment;
import com.example.BankEmitentService.repositories.BankEmitmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * This service class is responsible for validating card numbers
 * by checking them against bank emitment data.
 */
@Service
public class CardValidationService {

    private static final Logger logger = LoggerFactory.getLogger(CardValidationService.class);
    private final BankEmitmentRepository bankEmitmentRepository;

    /**
     * Constructs a CardValidationService with the specified BankEmitmentRepository.
     *
     * @param bankEmitmentRepository The repository used to access bank emitment data.
     */
    @Autowired
    CardValidationService(BankEmitmentRepository bankEmitmentRepository) {
        this.bankEmitmentRepository = bankEmitmentRepository;
    }

        /**
     * Checks the card range for a given card number.
     * <p>
     * Extracts the BIN from the card number, normalizes the
     * card number by replacing certain characters, and checks if the
     * card number falls within the defined range for the corresponding BIN.
     * <p>
     *  * Operates within a transaction with SERIALIZABLE isolation
     *  * to ensure data consistency and prevent phantom reads during the
     *  * validation process.
     *
     * @param cardNumber The card number to validate.
     *
     * @return An Optional containing a BankEmitmentDTO if the card is valid,
     *         or an empty Optional if no valid range is found.
     *
     * @throws IllegalArgumentException if the card number is null, empty,
     *         or not in a valid format.
     */
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Optional<BankEmitmentDTO> checkCardRange(String cardNumber) {
        validateCardNumber(cardNumber);

        int bin = extractBin(cardNumber);
        long cardAsLong = normalizeCardNumber(cardNumber);

        List<BankEmitment> emitments = bankEmitmentRepository.findByBin(bin);
        if (emitments.isEmpty()) {
            return Optional.empty();
        }

        BankEmitment emitment = emitments.getFirst();
        return validateCardRange(cardAsLong, emitment);
    }

    /**
     * Extracts the BIN from the card number.
     *
     * @param cardNumber The card number to extract the BIN from.
     * @return The extracted BIN.
     */
    private int extractBin(String cardNumber) {
        return Integer.parseInt(cardNumber.substring(0, 6));
    }

    /**
     * Normalizes the card number by replacing certain characters and ensuring it is a valid length.
     *
     * @param cardNumber The card number to normalize.
     * @return The normalized card number as a long.
     */
    private long normalizeCardNumber(String cardNumber) {
        String fullCardNumber = cardNumber.replace("*", "0");
        if (fullCardNumber.length() == 16) {
            fullCardNumber = fullCardNumber.concat("000");

            logger.info(STR."Normalized card format: \{fullCardNumber}");
        }
        else if (fullCardNumber.length() <= 16) {
            logger.error("The card number length must me at least 16 symbols!");
            throw new IllegalArgumentException();
        }

        try {
            return Long.parseLong(fullCardNumber);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid card number format.");
        }
    }

    /**
     * Validates the provided card number.
     *
     * @param cardNumber The card number to validate.
     * @throws IllegalArgumentException if the card number is null or empty.
     */
    private void validateCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.isEmpty())
            throw new IllegalArgumentException("Card number cannot be null or empty.");
        else logger.info(STR."Card is present: \{cardNumber}");
    }

    /**
     * Validates the card range against the emitment data.
     *
     * @param cardAsLong The normalized card number as a long.
     * @param emitment The BankEmitment entity to validate against.
     * @return An Optional containing a BankEmitmentDTO if the card is valid, or an empty Optional.
     */
    private Optional<BankEmitmentDTO> validateCardRange(long cardAsLong, BankEmitment emitment) {
        boolean checkMinRange = cardAsLong >= Long.parseLong(emitment.getMinRange());
        boolean checkMaxRange = cardAsLong <= Long.parseLong(emitment.getMaxRange());

        if (checkMinRange && checkMaxRange) {
            BankEmitmentDTO dto = BankEmitmentDTO.builder()
                    .bin(emitment.getBin())
                    .alphaCode(emitment.getAlphaCode())
                    .bankName(emitment.getBankName())
                    .build();

            logger.info("Card range is valid.");

            return Optional.of(dto);
        }
        logger.info("Card range is not valid. Card not found");

        return Optional.empty();
    }

}

/**
 * <==================================>
 * Copyright (c) 2024 Ilya Sukhina.*
 * <=================================>
 */

package com.example.BankEmitentService.controllers.api;

import com.example.BankEmitentService.dto.BankEmitmentDTO;
import com.example.BankEmitentService.services.CardValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * This controller handles requests related to bank emitment data.
 */
@RestController
@CrossOrigin(origins = "http://localhost:63342")
public class BankEmitmentController {

    private final CardValidationService cardValidationService;

    /**
     * Constructs a new instance of BankEmitmentController with the specified CardValidationService.
     *
     * @param cardValidationService the service used for validating card numbers and retrieving bank information
     */
    @Autowired
    BankEmitmentController(CardValidationService cardValidationService){
        this.cardValidationService =  cardValidationService;
    }

    /**
     * Validates a given card number and retrieves the corresponding bank emitment data.
     *
     * @param cardNumber the card number to validate
     * @return ResponseEntity containing BankEmitmentDTO if the card is valid,
     *         or a 404 Not Found response if the bank information is not available.
     */
    @PostMapping("/api/v1/card")
    public ResponseEntity<BankEmitmentDTO> postCardNumber(@RequestParam String cardNumber){

        Optional<BankEmitmentDTO> checkedCardRange = this.cardValidationService.checkCardRange(cardNumber);

        return checkedCardRange.map(ResponseEntity::ok)
                               .orElseGet(() -> ResponseEntity.notFound().build());
    }

}

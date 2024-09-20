package com.example.BankEmitentService.api;

import com.example.BankEmitentService.entity.BankEmitment;
import com.example.BankEmitentService.services.CardValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * <==================================>
 * Copyright (c) 2024 Ilya Sukhina.*
 * <=================================>
 */
@RestController
public class BankEmitmentController {

    private final CardValidationService cardValidationService;

    @Autowired
    BankEmitmentController(CardValidationService cardValidationService){
        this.cardValidationService =  cardValidationService;
    }

    @PostMapping("/api/v1/card")
    public ResponseEntity<BankEmitment> postCardNumber(@RequestParam String cardNumber){

        Optional<BankEmitment> checkedCardRange = this.cardValidationService.checkCardRange(cardNumber);

        return checkedCardRange.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


}

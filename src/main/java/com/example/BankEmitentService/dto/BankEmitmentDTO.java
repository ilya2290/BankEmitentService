/**
 * <==================================>
 * Copyright (c) 2024 Ilya Sukhina.*
 * <=================================>
 */

package com.example.BankEmitentService.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object for Bank Emitment.
 * This class is used to transfer data between the server and client.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BankEmitmentDTO {
    private int bin;
    private String alphaCode;
    private String bankName;
}

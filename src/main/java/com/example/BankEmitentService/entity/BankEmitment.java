/**
 * <==================================>
 * Copyright (c) 2024 Ilya Sukhina.*
 * <=================================>
 */

package com.example.BankEmitentService.entity;

import com.google.gson.annotations.SerializedName;
import jakarta.persistence.*;
import lombok.*;

/**
 * Represents a bank emitment entity in the database.
 **/
@Builder
@Table(name = "bank_emitments")
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
public class BankEmitment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column
    int bin;

    @Column(name = "min_range")
    @SerializedName("min_range")
    String minRange;

    @Column(name = "max_range")
    @SerializedName("max_range")
    String maxRange;

    @Column(name = "alpha_code")
    @SerializedName("alpha_code")
    String alphaCode;

    @Column(name = "bank_name")
    @SerializedName("bank_name")
    String bankName;
}

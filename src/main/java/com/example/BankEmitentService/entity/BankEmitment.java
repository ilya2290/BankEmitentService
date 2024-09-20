/**
 * <==================================>
 * Copyright (c) 2024 Ilya Sukhina.*
 * <=================================>
 */

package com.example.BankEmitentService.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    int id;

    @Column
    int bin;

    @Column(name = "min_range")
    @SerializedName("min_range")
    @JsonIgnore
    String minRange;

    @Column(name = "max_range")
    @SerializedName("max_range")
    @JsonIgnore
    String maxRange;

    @Column(name = "alpha_code")
    @SerializedName("alpha_code")
    String alphaCode;

    @Column(name = "bank_name")
    @SerializedName("bank_name")
    String bankName;
}

package com.example.BankEmitentService.services;

import com.example.BankEmitentService.entity.BankEmitment;
import com.example.BankEmitentService.repositories.BankEmitmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * <==================================>
 * Copyright (c) 2024 Ilya Sukhina.*
 * <=================================>
 */
@Service
public class CardValidationService {

    private final BankEmitmentRepository bankEmitmentRepository;

    @Autowired
    CardValidationService(BankEmitmentRepository bankEmitmentRepository){
        this.bankEmitmentRepository = bankEmitmentRepository;
    }

    public Optional<BankEmitment> checkCardRange(String cardNumber) {

        if (cardNumber == null || cardNumber.isEmpty()) {
            throw new IllegalArgumentException();
        }
        int bin = Integer.parseInt(cardNumber.substring(0, 6));

        //TODO проверить количество символов

        // Убираем любые символы-звезды (замены) и дополняем нулями до 19 символов
        String fullCardNumber = cardNumber.replace("*", "0"); // если хуйня заходит? заменить регуляркны

        if (fullCardNumber.length() == 16)
            fullCardNumber = fullCardNumber.concat("000");

        // Парсим номер карты в числовой формат (чтобы сравнивать диапазоны)
        long cardAsLong;

        try {
            cardAsLong = Long.parseLong(fullCardNumber);
        }
        catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid card number format.");
        }

        // Ищем по BIN

        List<BankEmitment> emitments = bankEmitmentRepository.findByBin(bin); // TODO проверить что у них одинаковые данные и только разные ID иначе фейл (транзакция)
        BankEmitment emitment = new BankEmitment();

        if(!emitments.isEmpty())
            emitment = emitments.getFirst();



       boolean checkMinRange = cardAsLong >= Long.parseLong(emitment.getMinRange());
       boolean checkMaxRange = cardAsLong <= Long.parseLong(emitment.getMaxRange());


        if (checkMinRange && checkMaxRange) {

                return Optional.ofNullable(BankEmitment.builder()
                                                        .bin(emitment.getBin())
                                                        .alphaCode(emitment.getAlphaCode())
                                                        .bankName(emitment.getBankName())
                                                        .build());
            }

        return Optional.empty();
    }

}

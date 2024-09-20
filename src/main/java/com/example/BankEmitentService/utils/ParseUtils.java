/**
 * <==================================>
 * Copyright (c) 2024 Ilya Sukhina.*
 * <=================================>
 */

package com.example.BankEmitentService.utils;

import com.example.BankEmitentService.entity.BankEmitment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import static com.example.BankEmitentService.constants.Constants.*;

/**
 * Utility class for parsing JSON files into a list of BankEmitment objects.
 */
public class ParseUtils {

    private static final Logger logger = LoggerFactory.getLogger(ParseUtils.class);

    /**
     * Returns a list of BankEmitment objects by parsing a predefined JSON file.
     *
     * @return List of BankEmitment objects containing parsed data from the JSON file.
     */
    public static List<BankEmitment> bankEmitments() {
        return jsonFileParser(BIN_INFO_JSON_PATH);
    }

    /**
     * Parses a JSON file and converts it into a list of BankEmitment objects.
     *
     * @param filePath The path to the JSON file to be parsed.
     *
     * @return A list of BankEmitment objects parsed from the JSON file. If an error occurs,
     * an empty list is returned.
     */
    public static List<BankEmitment> jsonFileParser(String filePath) {

        List<BankEmitment> bankInfoList = new ArrayList<>();

        try (FileReader reader = new FileReader(filePath)) {
            Gson gson = new Gson();
            Type bankListType = new TypeToken<List<BankEmitment>>() {}.getType();

            bankInfoList = gson.fromJson(reader, bankListType);

            logger.info(STR."Parse complete to file: \{filePath}");
        }
        catch (IOException e) {
            logger.error(STR."Failed to parse file! \{filePath}" + '\n' + e.getMessage());
        }
        return bankInfoList;
    }

}

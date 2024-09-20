/**
 * <==================================>
 * Copyright (c) 2024 Ilya Sukhina.*
 * <=================================>
 */

package com.example.BankEmitentService.constants;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * This class contains constants used for file handling and download paths
 * related to bank information processing.
 */
public final class Constants {

    public static final String BASE_FOLDER;
    public static final String BIN_INFO_JSON;
    public static final String BIN_INFO_ZIP;
    public static final String ONLINE_CATALOG_URI;
    public static final String ZIP_DOWNLOAD_FOLDER;
    public static final String ZIP_EXTRACT_FOLDER_PATH;
    public static final String ZIP_FILE_PATH;
    public static final String BIN_INFO_JSON_PATH;

    static {
        Properties properties = new Properties();
        try (InputStream input = Constants.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input != null) {
                properties.load(input);
                BASE_FOLDER = properties.getProperty("base.folder");
                BIN_INFO_JSON = properties.getProperty("bin.info.json");
                BIN_INFO_ZIP = properties.getProperty("bin.info.zip");
                ONLINE_CATALOG_URI = properties.getProperty("online.catalog.uri");
                ZIP_DOWNLOAD_FOLDER = BASE_FOLDER + File.separator + properties.getProperty("zip.download.folder");
                ZIP_EXTRACT_FOLDER_PATH = BASE_FOLDER + File.separator + properties.getProperty("zip.extract.folder.path");
                ZIP_FILE_PATH = ZIP_DOWNLOAD_FOLDER + File.separator + BIN_INFO_ZIP;
                BIN_INFO_JSON_PATH = ZIP_EXTRACT_FOLDER_PATH + File.separator + BIN_INFO_JSON;
            } else {
                throw new IOException("Unable to find config.properties");
            }
        } catch (IOException ex) {
            throw new RuntimeException("Failed to load configuration", ex);
        }
    }

    /**
     * Private constructor to prevent class instances from being created.
    **/
    private Constants() {
    }
}

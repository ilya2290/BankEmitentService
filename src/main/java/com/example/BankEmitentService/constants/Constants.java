package com.example.BankEmitentService.constants;

import java.io.File;

/**
 * <==================================>
 * Copyright (c) 2024 Ilya Sukhina.*
 * <=================================>
 */
public final class Constants {


    // СДЕЛАТЬ РАЗВИЛКУ НА ОС ЧТОБЫ СОЗДАВАТЬ ФАЙЛ
    public static final String BASE_FOLDER = STR."C:\{File.separator}BankInfo";

    public static final String BIN_INFO_JSON = "bininfo.json";
    public static final String BIN_INFO_ZIP = "bininfo.json.zip";
    public static final String ZIP_EXTRACT_FOLDER_PATH = STR."\{BASE_FOLDER}\{File.separator}ZipOutput"; //TODO MAC/WIN

    public static final String ZIP_DOWNLOAD_FOLDER = STR."\{BASE_FOLDER}\{File.separator}ZipDownload";

    public static final String ONLINE_CATALOG_URI = "https://ecom-bininfo.s3.eu-west-1.amazonaws.com/bininfo.json.zip";

    public static final String ZIP_FILE_PATH = ZIP_DOWNLOAD_FOLDER + File.separator + BIN_INFO_ZIP; //TODO

    public static final String BIN_INFO_JSON_PATH =  ZIP_EXTRACT_FOLDER_PATH + File.separator + BIN_INFO_JSON;


}

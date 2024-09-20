/**
 * <==================================>
 * Copyright (c) 2024 Ilya Sukhina.*
 * <=================================>
 */

package com.example.BankEmitentService.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.zip.*;

import static com.example.BankEmitentService.constants.Constants.*;


public class UnzipUtils {

    private static final Logger logger = LoggerFactory.getLogger(UnzipUtils.class);

    private static void checkUnpackingDirExists(String destinationDir, File dir) throws IOException {
        if (!dir.exists() && !dir.mkdirs())
            throw new IOException(STR."Failed to create directory for unpacking: \{destinationDir}");
        else
            logger.info(STR."The unpacking directory has been created: \{destinationDir}");
    }

    public static void createDirectoryIfNotExists(String dirPath) {
        File directory = new File(dirPath);

        if (!directory.exists()) {
            if (directory.mkdirs())
                logger.info(STR."Directory successfully created: \{dirPath}");
            else
                logger.error(STR."Failed to create directory: \{dirPath}");
        }
        else
            logger.warn(STR."Directory already exists: \{dirPath}");
    }

    public static void downloadAndUnzip() {
        try {
            downloadZipFile(ONLINE_CATALOG_URI, ZIP_DOWNLOAD_FOLDER);
            unzip(ZIP_FILE_PATH, ZIP_EXTRACT_FOLDER_PATH);

            logger.info("Downloading and unzipping ended.");
        }
        catch (IOException | URISyntaxException e) {
            logger.error(e.getMessage());
        }
    }

//    public static void downloadZipFile(String fileURL, String saveDir) throws IOException, URISyntaxException {
//        URI uri = new URI(fileURL);
//        URL url = uri.toURL();
//        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
//        int responseCode = httpConn.getResponseCode();
//
//        if (responseCode == HttpURLConnection.HTTP_OK) {
//            String fileName = "";
//            String disposition = httpConn.getHeaderField("Content-Disposition");
//
//            if (disposition != null) {
//                int index = disposition.indexOf("filename=");
//                if (index > 0) {
//                    fileName = disposition.substring(index + 10, disposition.length() - 1);
//                }
//            } else {
//                fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1);
//            }
//
//            InputStream inputStream = httpConn.getInputStream();
//
//            createDirectoryIfNotExists(saveDir);
//
//            String saveFilePath = saveDir + File.separator + fileName;
//
//            FileOutputStream outputStream = new FileOutputStream(saveFilePath);
//
//            int bytesRead = -1;
//            byte[] buffer = new byte[4096];
//            while ((bytesRead = inputStream.read(buffer)) != -1) {
//                outputStream.write(buffer, 0, bytesRead);
//            }
//
//            outputStream.close();
//            inputStream.close();
//
//            logger.info(STR."File downloaded: \{saveFilePath}");
//        }
//        else {
//            logger.error(STR."Access to file denied. The server responded with the code: \{responseCode}");
//        }
//        httpConn.disconnect();
//    }

    public static void downloadZipFile(String fileURL, String saveDir) throws IOException, URISyntaxException {
        URI uri = new URI(fileURL);
        URL url = uri.toURL();
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        int responseCode = httpConn.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            String fileName = getFileName(httpConn, fileURL);
            InputStream inputStream = httpConn.getInputStream();

            createDirectoryIfNotExists(saveDir);
            String saveFilePath = saveDir + File.separator + fileName;

            saveFile(inputStream, saveFilePath);

            logger.info("File downloaded: {}", saveFilePath);
        } else {
            logger.error("Access to file denied. The server responded with the code: {}", responseCode);
        }
        httpConn.disconnect();
    }

    private static String getFileName(HttpURLConnection httpConn, String fileURL) {
        String fileName = "";
        String disposition = httpConn.getHeaderField("Content-Disposition");

        if (disposition != null) {
            int index = disposition.indexOf("filename=");
            if (index > 0) {
                fileName = disposition.substring(index + 10, disposition.length() - 1);
            }
        } else {
            fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1);
        }

        return fileName;
    }

    private static void saveFile(InputStream inputStream, String saveFilePath) throws IOException {
        try (FileOutputStream outputStream = new FileOutputStream(saveFilePath)) {
            int bytesRead;
            byte[] buffer = new byte[4096];
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
    }




    private static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());

        String destinationDirPath = destinationDir.getCanonicalPath();
        String destinationFilePath = destFile.getCanonicalPath();

        if (!destinationFilePath.startsWith(destinationDirPath + File.separator)) {
            throw new IOException(STR."Trying to unzip a file outside the target directory: \{zipEntry.getName()}");
        }

        return destFile;
    }

//    public static void unzip(String zipFilePath, String destinationDirectory) throws IOException {
//        File directory = new File(destinationDirectory);
//
//        checkUnpackingDirExists(destinationDirectory, directory);
//
//        byte[] buffer = new byte[1024];
//
//        ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFilePath));
//
//        ZipEntry zipEntry = zis.getNextEntry();
//
//        while (zipEntry != null) {
//            File newFile = newFile(directory, zipEntry);
//
//            if (zipEntry.isDirectory()) {
//                if (!newFile.isDirectory() && !newFile.mkdirs()) {
//                    throw new IOException(STR."Failed to create directory \{newFile}");
//                }
//            } else {
//                File parent = newFile.getParentFile();
//
//                if (!parent.isDirectory() && !parent.mkdirs()) {
//                    throw new IOException(STR."Failed to create directory for file \{newFile}");
//                }
//
//                try (FileOutputStream fos = new FileOutputStream(newFile)) {
//                    int len;
//                    while ((len = zis.read(buffer)) > 0) {
//                        fos.write(buffer, 0, len);
//                    }
//                }
//            }
//            zipEntry = zis.getNextEntry();
//        }
//        zis.closeEntry();
//        zis.close();
//        System.out.println("Unpacking complete.");
//    }

    public static void unzip(String zipFilePath, String destinationDirectory) throws IOException {
        File directory = new File(destinationDirectory);
        checkUnpackingDirExists(destinationDirectory, directory);

        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFilePath))) {
            ZipEntry zipEntry;
            while ((zipEntry = zis.getNextEntry()) != null) {
                processZipEntry(directory, zis, zipEntry);
            }
        }

        logger.info("Unpacking complete.");
    }

    private static void processZipEntry(File directory, ZipInputStream zis, ZipEntry zipEntry) throws IOException {
        File newFile = newFile(directory, zipEntry);

        if (zipEntry.isDirectory()) {
            if (!newFile.isDirectory() && !newFile.mkdirs()) {
                throw new IOException(STR."Failed to create directory \{newFile}");
            }
        } else {
            createParentDirectory(newFile);
            writeFile(zis, newFile);
        }
    }

    private static void createParentDirectory(File newFile) throws IOException {
        File parent = newFile.getParentFile();
        if (!parent.isDirectory() && !parent.mkdirs()) {
            throw new IOException(STR."Failed to create directory for file \{newFile}");
        }
    }

    private static void writeFile(ZipInputStream zis, File newFile) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(newFile)) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = zis.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }
        }
    }




}

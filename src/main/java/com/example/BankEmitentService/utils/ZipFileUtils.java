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

/**
 * Utility class for downloading and extracting ZIP files.
 */
public class ZipFileUtils {

    private static final Logger logger = LoggerFactory.getLogger(ZipFileUtils.class);

    /**
     * Checks if the specified unpacking directory exists and creates it if it doesn't.
     *
     * @param destinationDir The path of the directory to check/create.
     * @param dir The File object representing the directory.
     * @throws IOException if the directory cannot be created.
     */
    private static void checkUnpackingDirExists(String destinationDir, File dir) throws IOException {
        if (!dir.exists() && !dir.mkdirs())
            throw new IOException(STR."Failed to create directory for unpacking: \{destinationDir}");
        else
            logger.info(STR."The unpacking directory has been created: \{destinationDir}");
    }

    /**
     * Creates the parent directory for a given file if it doesn't exist.
     *
     * @param newFile The file for which the parent directory needs to be created.
     * @throws IOException if the parent directory cannot be created.
     */
    private static void createParentDirectory(File newFile) throws IOException {
        File parent = newFile.getParentFile();
        if (!parent.isDirectory() && !parent.mkdirs()) {
            throw new IOException(STR."Failed to create directory for file \{newFile}");
        }
    }

    /**
     * Creates a directory at the specified path if it does not already exist.
     *
     * @param dirPath The path of the directory to create.
     */
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

    /**
     * Downloads a ZIP file from the specified URL and extracts its contents.
     */
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

    /**
     * Downloads a ZIP file from the specified URL and saves it to the specified directory.
     *
     * @param fileURL The URL of the ZIP file to download.
     * @param saveDir The directory to save the downloaded file.
     * @throws IOException if an I/O error occurs during the download.
     * @throws URISyntaxException if the URL is malformed.
     */
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

    /**
     * Extracts the file name from the HTTP response headers or defaults to the last part of the URL.
     *
     * @param httpConn The HttpURLConnection to get the response from.
     * @param fileURL The URL of the file being downloaded.
     * @return The file name extracted from the response headers or URL.
     */
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

    /**
     * Saves the downloaded file to the specified path.
     *
     * @param inputStream The InputStream from which to read the data.
     * @param saveFilePath The path where the file will be saved.
     * @throws IOException if an I/O error occurs during saving.
     */
    private static void saveFile(InputStream inputStream, String saveFilePath) throws IOException {
        try (FileOutputStream outputStream = new FileOutputStream(saveFilePath)) {
            int bytesRead;
            byte[] buffer = new byte[4096];
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
    }

    /**
     * Creates a new file based on the ZIP entry and checks if it's within the target directory.
     *
     * @param destinationDir The target directory for the extracted files.
     * @param zipEntry The ZIP entry representing the file being extracted.
     * @return The new File object for the extracted file.
     * @throws IOException if the file path is outside the target directory.
     */
    private static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());

        String destinationDirPath = destinationDir.getCanonicalPath();
        String destinationFilePath = destFile.getCanonicalPath();

        if (!destinationFilePath.startsWith(destinationDirPath + File.separator)) {
            throw new IOException(STR."Trying to unzip a file outside the target directory: \{zipEntry.getName()}");
        }

        return destFile;
    }

    /**
     * Unzips a ZIP file to the specified directory.
     *
     * @param zipFilePath The path to the ZIP file to be extracted.
     * @param destinationDirectory The directory where the files will be extracted.
     * @throws IOException if an I/O error occurs during unzipping.
     */
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

    /**
     * Processes each entry in the ZIP file, creating directories or files as needed.
     *
     * @param directory The target directory for extracted files.
     * @param zis The ZipInputStream to read from.
     * @param zipEntry The current entry in the ZIP file.
     * @throws IOException if an I/O error occurs during processing.
     */
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

    /**
     * Writes the content of a ZipInputStream to a new file.
     *
     * @param zis The ZipInputStream from which to read data.
     * @param newFile The file to which the data will be written.
     * @throws IOException if an I/O error occurs during writing.
     */
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

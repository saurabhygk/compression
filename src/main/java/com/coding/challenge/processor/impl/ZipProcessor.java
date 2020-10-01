package com.coding.challenge.processor.impl;

import com.coding.challenge.processor.AbstractCompressionProcessor;
import org.apache.commons.io.output.CountingOutputStream;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.*;

public class ZipProcessor extends AbstractCompressionProcessor {

    private static final String COMPRESSED_FILE_NAME_PREFIX = "compressed-";
    private static final long ZIP_FILE_HEADER_SIZE = convertMBtoBytes(0.02);

    public int sequenceNumber = 1;
    private CountingOutputStream outStream;

    public void compress(String inputDir, String outputDir, long fileSize, String fileExtension)
            throws IOException {

        List<File> files = getAllFiles(inputDir);
        String compressFilePath = generateCompressedFileNameWithPath(outputDir, sequenceNumber++, fileExtension);
        FileOutputStream fos = new FileOutputStream(compressFilePath);
        this.outStream = new CountingOutputStream(fos);
        ZipOutputStream zipOut = new ZipOutputStream(this.outStream);

        FileInputStream fis;
        for (File file : files) {
            fis = new FileInputStream(file);
            zipOut.putNextEntry(new ZipEntry(getPath(inputDir, file.getPath())));

            byte[] bytes = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(bytes)) >= 0) {
                if (availableBytesInStream(fileSize) - ZIP_FILE_HEADER_SIZE > bytesRead) {
                    zipOut.write(bytes, 0, bytesRead);
                } else {
                    zipOut.closeEntry();
                    zipOut.finish();
                    closeOutStream(this.outStream);
                    // exceeded max size, start to read next bytes and created another compressed file
                    compressFilePath = generateCompressedFileNameWithPath(outputDir, sequenceNumber++, fileExtension);
                    fos = new FileOutputStream(compressFilePath);
                    this.outStream = new CountingOutputStream(fos);
                    zipOut = new ZipOutputStream(this.outStream);
                    zipOut.putNextEntry(new ZipEntry(getPath(inputDir, file.getPath())));
                    zipOut.write(bytes, 0, bytesRead);
                }
            }
            closeFileInputStream(fis);
        }
        closeZipOut(zipOut);
        closeOutStream(this.outStream);
        closeFileOutputStream(fos);
    }

    public void decompress(String inputDir, String outputDir, String compressedFileExtension) {
        List<File> zipFiles = getFilesWithExtensions(inputDir, compressedFileExtension);
        Map<String, FileOutputStream> fileOutputStreamMap = new ConcurrentHashMap<>();

        File file = new File(outputDir);
        if (!file.exists()) {
            file.mkdir();
        }

        FileInputStream fileInputStream;
        ZipInputStream zipInputStream;

        for (File zipFile : zipFiles) {
            try {
                fileInputStream = new FileInputStream(zipFile);
                zipInputStream = new ZipInputStream(fileInputStream);

                ZipEntry zipEntry;
                FileOutputStream fileOutputStream;
                while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                    File originalFile = new File(outputDir + zipEntry.getName());
                    if (zipEntry.isDirectory()) {
                        originalFile.mkdir();
                    } else {
                        originalFile.getParentFile().mkdirs();
                        originalFile.createNewFile();

                        if (fileOutputStreamMap.containsKey(zipEntry.getName())) {
                            fileOutputStream = fileOutputStreamMap.get(zipEntry.getName());
                        } else {
                            fileOutputStream = new FileOutputStream(originalFile);
                            fileOutputStreamMap.put(zipEntry.getName(), fileOutputStream);
                        }

                        byte[] bytes = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = zipInputStream.read(bytes)) >= 0) {
                            fileOutputStream.write(bytes, 0, bytesRead);
                        }
                    }
                }
                zipInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getPath(String basePath, String absolutePath) {
        if (absolutePath.startsWith(basePath)) {
            return absolutePath.substring(basePath.length());
        }
        return absolutePath;
    }

    private String generateCompressedFileNameWithPath(String directory, int sequenceNumber, String fileExtension) {
        return new StringBuilder()
                .append(directory)
                .append(COMPRESSED_FILE_NAME_PREFIX)
                .append(sequenceNumber)
                .append(fileExtension).toString();
    }

    private long availableBytesInStream(long maxFileSizeBytes) {
        return maxFileSizeBytes - this.outStream.getByteCount();
    }

    private void closeZipOut(ZipOutputStream zipOut) throws IOException {
        if (zipOut != null) {
            zipOut.closeEntry();
            zipOut.finish();
        }
    }

    private void closeOutStream(CountingOutputStream outStream) throws IOException {
        if (outStream != null) {
            outStream.close();
        }
    }

    private void closeFileInputStream(FileInputStream fileInputStream) throws IOException {
        if (fileInputStream != null) {
            fileInputStream.close();
        }
    }

    private void closeFileOutputStream(FileOutputStream fileOutputStream) throws IOException {
        if (fileOutputStream != null) {
            fileOutputStream.close();
        }
    }
}

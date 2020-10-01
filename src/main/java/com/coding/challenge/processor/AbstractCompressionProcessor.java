package com.coding.challenge.processor;

import com.coding.challenge.constants.SupportedAlgorithms;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.*;
import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractCompressionProcessor {
    public static final long MEGABYTE = 1000L * 1000L;

    public abstract void compress(String inputDir, String outputDir, long fileSize, String compressedFileExtension)
            throws IOException;

    public abstract void decompress(String inputDir, String outputDir, String fileExtensionToDecompress);

    public static AbstractCompressionProcessor getProcessor(String supportedAlgorithm) {
        if (SupportedAlgorithms.ZIP.name().equalsIgnoreCase(supportedAlgorithm)) {
            System.out.println("Started compression/decompression using ZIP compression algorithm.");
            return SupportedAlgorithms.ZIP.getProcessor();
        } else {
            throw new UnsupportedOperationException("Unsupported compression algorithm:" + supportedAlgorithm);
        }
    }

    public static List<File> getAllFiles(String directory) {
        List<File> allFiles = new ArrayList<>();
        try {
            allFiles = Files.walk(Paths.get(directory))
                    .filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return allFiles;
    }

    public static List<File> getFilesWithExtensions(String directory, String fileExtension) {
        File file = new File(directory);
        List<File> files = Arrays.asList(file.listFiles((dir, name) -> name.endsWith(fileExtension)));
        files.sort((file1, file2) -> {
            try {
                BasicFileAttributes sourceBasicFileAttribute =
                        Files.getFileAttributeView(file1.toPath(), BasicFileAttributeView.class).readAttributes();

                BasicFileAttributes destinationBasicFileAttribute =
                        Files.getFileAttributeView(file2.toPath(), BasicFileAttributeView.class).readAttributes();

                return sourceBasicFileAttribute.creationTime().compareTo(destinationBasicFileAttribute.creationTime());
            } catch (IOException e) {
                e.printStackTrace();
                return 1;
            }
        });
        return files;
    }

    public static long convertMBtoBytes(Double MB) {
        return (long) (MB * MEGABYTE);
    }

    public static boolean compareDirectoriesAfterDecompression(String sourceDir, String decompressedDir) {
        List<File> sourceFiles = getAllFiles(sourceDir);
        List<File> destinationFiles = getAllFiles(decompressedDir);
        if (sourceFiles.size() != destinationFiles.size()) {
            return false;
        }
        for (int i = 0; i < sourceFiles.size(); i++) {
            if (sourceFiles.get(i).length() != destinationFiles.get(i).length()) {
                return false;
            }
        }
        return true;
    }
}

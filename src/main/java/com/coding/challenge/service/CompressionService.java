package com.coding.challenge.service;

import com.coding.challenge.constants.SupportedAlgorithms;
import com.coding.challenge.exception.NotValidDecompressionException;
import com.coding.challenge.processor.AbstractCompressionProcessor;

import java.io.IOException;

import static com.coding.challenge.constants.SupportedAlgorithms.ZIP;
import static com.coding.challenge.processor.AbstractCompressionProcessor.*;

public class CompressionService {

    private final String[] arguments;

    public CompressionService(String[] arguments) {
        this.arguments = arguments;
    }

    public void process() throws IOException {
        long startTime = System.nanoTime();
        String inputDir = this.arguments[0];
        String outputDir = this.arguments[1];

        if (this.arguments.length == 3 && "validate".equalsIgnoreCase(this.arguments[2])) {
            if(!compareDirectoriesAfterDecompression(inputDir, outputDir)) {
                throw new NotValidDecompressionException("Comparison failed with decompressed directory.");
            }
            System.out.println("Validate successfully, input directory: " + inputDir + " and output directory"
                    + outputDir);
            return;
        } else if (this.arguments.length >= 3) {
            long fileSize = convertMBtoBytes(Double.parseDouble(this.arguments[2]));
            // ZIP is default supported compression algorithm
            String supportCompressionAlgo = this.arguments.length == 4 ? this.arguments[3] : ZIP.name();
            String compressToFileExtension = SupportedAlgorithms.getSupportedFileExtension(supportCompressionAlgo);
            AbstractCompressionProcessor processor = AbstractCompressionProcessor.getProcessor(supportCompressionAlgo);
            System.out.println("Compression: input directory: " + inputDir + ", output directory" + outputDir);
            processor.compress(inputDir, outputDir, fileSize, compressToFileExtension);
            System.out.println("Compression finished in: " + (System.nanoTime() - startTime) / 1000000 + " ms");
        } else {
            // ZIP is default supported compression algorithm
            String supportCompressionAlgo = this.arguments.length == 3 ? this.arguments[2] : ZIP.name();
            String compressedFileExtension = SupportedAlgorithms.getSupportedFileExtension(supportCompressionAlgo);
            AbstractCompressionProcessor processor = AbstractCompressionProcessor.getProcessor(supportCompressionAlgo);
            System.out.println("Decompression: input directory: " + inputDir + ", output directory" + outputDir);
            processor.decompress(inputDir, outputDir, compressedFileExtension);
            System.out.println("Decompression finished in " + (System.nanoTime() - startTime) / 1000000 + " ms");
        }
        System.out.println("Maximum memory used (MB):" + bytesToMB(Runtime.getRuntime().totalMemory()));
    }

    public static long bytesToMB(long bytes) {
        return bytes / MEGABYTE;
    }
}

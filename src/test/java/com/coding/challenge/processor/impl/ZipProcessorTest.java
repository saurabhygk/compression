package com.coding.challenge.processor.impl;

import org.apache.commons.io.FileUtils;
import org.junit.*;

import java.io.*;
import java.util.List;

import static com.coding.challenge.processor.AbstractCompressionProcessor.*;
import static org.junit.Assert.*;

public class ZipProcessorTest {

    private static final String INPUT_DIR = "src/test/resources/data";
    private static final String OUTPUT_DIR = "src/test/resources/compressed/";
    private static final String DECOMPRESSED_DIR = "src/test/resources/decompressed/";
    private static long MAX_SIZE = convertMBtoBytes(5.0);

    private ZipProcessor processor;

    private File out;
    File decompressed;

    @Before
    public void instantiate() throws IOException {
        out = new File(OUTPUT_DIR);
        decompressed = new File(DECOMPRESSED_DIR);
        if (!out.exists())
            out.mkdir();

        if (!decompressed.exists())
            decompressed.mkdir();

        FileUtils.cleanDirectory(out);
        FileUtils.cleanDirectory(decompressed);

        processor = new ZipProcessor();
    }

    @Test
    public void compression() throws IOException {

        processor.compress(INPUT_DIR, OUTPUT_DIR, MAX_SIZE, ".zip");

        processor.decompress(OUTPUT_DIR, DECOMPRESSED_DIR, ".zip");

        assertTrue(compareDirectoriesAfterDecompression(INPUT_DIR, DECOMPRESSED_DIR));
        assertTrue(compareCompressedFileSizeWithMaxSize(OUTPUT_DIR, MAX_SIZE));
    }

    private static boolean compareCompressedFileSizeWithMaxSize(String sourceDir, long maxSize) {
        List<File> files = getAllFiles(sourceDir);
        for (File file : files) {
            if (file.length() > maxSize) {
                return false;
            }
        }
        return true;
    }
}
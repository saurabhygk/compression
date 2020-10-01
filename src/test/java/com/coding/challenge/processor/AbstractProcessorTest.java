package com.coding.challenge.processor;

import org.apache.commons.io.FileUtils;
import org.junit.*;

import java.io.*;
import java.util.List;

import static com.coding.challenge.processor.AbstractCompressionProcessor.*;
import static org.junit.Assert.assertEquals;

public class AbstractProcessorTest {

    private static final String TEST_DIR = "src/test/resources/testDir/";

    @Before
    public void instantiate() throws IOException {
        File testDir = new File(TEST_DIR);
        if (!testDir.exists())
            testDir.mkdir();

        FileUtils.cleanDirectory(new File(TEST_DIR));

        File txtFile = new File(TEST_DIR.concat("test.txt"));
        File zipFile = new File(TEST_DIR.concat("test.zip"));

        txtFile.getParentFile().mkdirs();
        txtFile.createNewFile();

        zipFile.getParentFile().mkdirs();
        zipFile.createNewFile();

    }

    @Test
    public void testGetAllFiles() {
        List<File> files = getAllFiles(TEST_DIR);
        assertEquals(files.size(), 2);
    }

    @Test
    public void testGetAllZipFiles() {
        List<File> files = getFilesWithExtensions(TEST_DIR, ".zip");
        assertEquals(files.size(), 1);
    }

    @Test
    public void testMBtoBytes() {
        long bytes = convertMBtoBytes(2.0);
        assertEquals(bytes, (long) (2.0 * MEGABYTE));
    }
}
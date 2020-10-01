package com.coding.challenge.constants;

import com.coding.challenge.exception.UnsupportedCompressionAlgorithm;
import com.coding.challenge.processor.AbstractCompressionProcessor;
import com.coding.challenge.processor.impl.ZipProcessor;

public enum SupportedAlgorithms {
    ZIP(new ZipProcessor(), ".zip");

    private AbstractCompressionProcessor processor;
    private String fileExtension;

    SupportedAlgorithms(AbstractCompressionProcessor processor, String fileExtension) {
        this.processor = processor;
        this.fileExtension = fileExtension;
    }

    public static String getSupportedFileExtension(String supportCompressionAlgo) {
        for(SupportedAlgorithms supportedAlgorithm : SupportedAlgorithms.values()) {
            if (supportedAlgorithm.name().equalsIgnoreCase(supportCompressionAlgo)) {
                return supportedAlgorithm.getFileExtension();
            }
        }
        throw new UnsupportedCompressionAlgorithm("Compression Algorithm [" + supportCompressionAlgo
                + "] not supported");
    }

    public AbstractCompressionProcessor getProcessor() {
        return processor;
    }

    public String getFileExtension() {
        return fileExtension;
    }
}

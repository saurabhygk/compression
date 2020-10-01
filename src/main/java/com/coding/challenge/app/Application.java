package com.coding.challenge.app;

import com.coding.challenge.service.CompressionService;
import com.coding.challenge.validator.Validator;

import java.io.IOException;

public class Application {

    public static void main(String[] args) throws IOException {
        Validator validator = new Validator(args);
        validator.validateArguments();
        CompressionService compressionService = new CompressionService(args);
        compressionService.process();
    }
}

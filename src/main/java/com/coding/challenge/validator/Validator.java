package com.coding.challenge.validator;

import com.coding.challenge.exception.InvalidArgumentsException;

import java.nio.file.*;

public class Validator {

    private final String[] arguments;

    public Validator(String[] arguments) {
        this.arguments = arguments;
    }

    public void validateArguments() {
        if (this.arguments.length < 2) {
            throw new InvalidArgumentsException("Incorrect number of arguments provided");
        }

        if (!Files.isDirectory(Paths.get(this.arguments[0])) ||
                !Files.isDirectory(Paths.get(this.arguments[1]))) {
            throw new InvalidArgumentsException("Input/output directory is invalid");
        }
    }
}

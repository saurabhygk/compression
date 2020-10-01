package com.coding.challenge.validator;

import com.coding.challenge.exception.InvalidArgumentsException;
import org.junit.Test;

public class ValidatorTest  {

    @Test(expected = InvalidArgumentsException.class)
    public void throwExceptionIfArgumentsLessThanTwo() {
        Validator validator = new Validator(new String[] {});
        validator.validateArguments();
    }

    @Test(expected = InvalidArgumentsException.class)
    public void throwExceptionIfInputDirectoryNotValid() {
        String input = "src/test/resources/invalid";
        String output = "src/test/resources/invalid";
        Validator validator = new Validator(new String[] {input, output});
        validator.validateArguments();
    }
}
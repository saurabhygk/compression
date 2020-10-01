package com.coding.challenge;

import com.coding.challenge.processor.AbstractProcessorTest;
import com.coding.challenge.processor.impl.ZipProcessorTest;
import com.coding.challenge.validator.ValidatorTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({
        AbstractProcessorTest.class,
        ValidatorTest.class,
        ZipProcessorTest.class
})

public class UnitTestSuite {
}

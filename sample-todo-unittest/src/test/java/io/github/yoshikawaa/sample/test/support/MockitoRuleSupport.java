package io.github.yoshikawaa.sample.test.support;

import org.junit.Rule;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public abstract class MockitoRuleSupport {

    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();
}

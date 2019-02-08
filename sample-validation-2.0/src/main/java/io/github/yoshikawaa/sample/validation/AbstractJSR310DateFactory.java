package io.github.yoshikawaa.sample.validation;

import java.time.Instant;
import java.util.Date;

import org.terasoluna.gfw.common.date.DefaultClassicDateFactory;

public abstract class AbstractJSR310DateFactory extends DefaultClassicDateFactory implements JSR310DateFactory {

    @Override
    public Date newDate() {
        return Date.from(Instant.now(newClock()));
    }
}

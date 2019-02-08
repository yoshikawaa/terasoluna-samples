package io.github.yoshikawaa.sample.validation;

import java.time.Clock;

import javax.validation.ClockProvider;

import lombok.Setter;

public class DateFactoryClockProvider implements ClockProvider {

    @Setter
    private JSR310DateFactory dateFactory;
    
    @Override
    public Clock getClock() {
        return dateFactory.newClock();
    }

}

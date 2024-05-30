package com.example.demo.alarmClock.helpers;


import com.example.demo.alarmClock.enums.AlarmState;
import com.example.demo.alarmClock.enums.Frequency;
import com.example.demo.alarmClock.model.Alarm;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AlarmBuilder {

    private final UUID id = UUID.randomUUID();
    private String name = "Alarm";
    private Frequency frequency = Frequency.ONCE;
    private Integer time = Instant.now().getNano();
    private Integer snoozeTime = 5;
    private List<Integer> days = new ArrayList<>();

    public static AlarmBuilder builder() {
        return new AlarmBuilder();
    }

    public AlarmBuilder name(String name) {
        this.name = name;
        return this;
    }

    public AlarmBuilder frequency(Frequency frequency) {
        this.frequency = frequency;
        return this;
    }

    public AlarmBuilder days(List<Integer> days) {
        this.days = days;
        return this;
    }

    public AlarmBuilder time(Integer time) {
        this.time = time;
        return this;
    }

    public AlarmBuilder snoozeTime(Integer snoozeTime) {
        this.snoozeTime = snoozeTime;
        return this;
    }

    public Alarm build() {
        this.validate();
        return Alarm.builder()
                .id(id)
                .name(name)
                .state(AlarmState.NEW)
                .frequency(frequency)
                .days(days)
                .time(time)
                .endTime(getEndTime())
                .snoozeTime(snoozeTime)
                .nextRingTime(time)
                .ringTone("Hello")
                .snoozeTime(5)
                .build();
    }

    private void validate() {
        if (frequency.equals(Frequency.ONCE) && !days.isEmpty()) {
            throw new RuntimeException("Days must be empty for once frequency.");
        }

        if (this.time < Instant.now().getEpochSecond()) {
            throw new RuntimeException("Time must be in the future");
        }

    }

    private Integer getEndTime() {
        if (this.frequency == Frequency.ONCE) {
            return this.time;
        }
        return Integer.MAX_VALUE;
    }
}

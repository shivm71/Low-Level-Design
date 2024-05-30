package com.example.demo.alarmClock.model;


import com.example.demo.alarmClock.enums.AlarmState;
import com.example.demo.alarmClock.enums.Frequency;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Builder
@Data
public class Alarm {
    private UUID id;
    private String name;
    private AlarmState state;
    private Frequency frequency;
    private List<Integer> days;
    private Integer time;
    private Integer nextRingTime;
    private Integer endTime;
    private String ringTone;
    // in Minutes
    private boolean isSnoozed;
    private Integer snoozeTime;
    @Builder.Default
    private int snoozeCount = 0;

    public boolean isRepeat() {
        return frequency != Frequency.ONCE;
//        return time < endTime;
    }

    public Integer calculateNextRingTime() {
        int currentDay = Date.from(Instant.ofEpochMilli(Long.valueOf(time))).getDay();
        if (!isRepeat()) {
            return 0;
        }
        if (frequency.equals(Frequency.DAILY)) {
            time = time + 24 * 60 * 60;
            nextRingTime = time;
            return time;
        }

        if (frequency.equals(Frequency.WEEKLY)) {

            time = time + 7 * 24 * 60 * 60;
            return time;
        }

        if (frequency.equals(Frequency.MONTHLY)) {
            time = time + 30 * 24 * 60 * 60;
            return time;
        }
        return 0;

//        if (frequency.equals(Frequency.CUSTOM)) {
//            Integer day = Date.from(Instant.ofEpochMilli(Long.valueOf(time))).getDay();
//        }
    }

}

package com.example.demo.alarmClock;


import com.example.demo.alarmClock.enums.Frequency;
import com.example.demo.alarmClock.helpers.AlarmBuilder;
import com.example.demo.alarmClock.model.Alarm;
import com.example.demo.alarmClock.service.AlarmService;

import java.time.Instant;

public class Main {

    public static void main(String[] args) {
        System.out.println("This is basic Alarm clock");
        System.out.println(Instant.now().getEpochSecond());
        Alarm alarm = AlarmBuilder.builder()
                .frequency(Frequency.ONCE)
                .time((int) Instant.now()
                        .plusSeconds(1 * 1 * 1 * 10).getEpochSecond()).build();
        System.out.println(alarm);
        Alarm alarm1 = AlarmBuilder.builder()
                .frequency(Frequency.ONCE)
                .time((int) Instant.now()
                        .plusSeconds(1 * 1 * 1 * 11).getEpochSecond()).build();
        System.out.println(alarm1);

        AlarmService alarmService = new AlarmService();
        alarmService.addAlarm(alarm);
        alarmService.addAlarm(alarm1);
    }
}



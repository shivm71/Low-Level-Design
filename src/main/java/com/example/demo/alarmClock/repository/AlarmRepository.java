package com.example.demo.alarmClock.repository;


import com.example.demo.alarmClock.model.Alarm;

import java.util.HashMap;
import java.util.UUID;

public class AlarmRepository {

    private static AlarmRepository alarmRepository = new AlarmRepository();
    HashMap<UUID, Alarm> alarmDB = new HashMap<>();

    private AlarmRepository() {
    }

    public static AlarmRepository getInstance() {
        return alarmRepository;
    }

    public Alarm getAlarm(UUID id) {
        return alarmDB.get(id);
    }

    public void addAlarm(Alarm alarm) {
        alarmDB.put(alarm.getId(), alarm);
    }

    public void deleteAlarm(UUID id) {
        alarmDB.remove(id);
    }

    public void updateAlarm(Alarm alarm) {
        addAlarm(alarm);
    }

}

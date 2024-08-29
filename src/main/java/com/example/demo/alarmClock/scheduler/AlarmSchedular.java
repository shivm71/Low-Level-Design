package com.example.demo.alarmClock.scheduler;


import com.example.demo.alarmClock.model.Alarm;
import com.example.demo.alarmClock.service.AlarmService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListMap;

public class AlarmSchedular {
    private final TickService tickService;
    // Long for time
    private ConcurrentSkipListMap<Integer, List<UUID>> ringTimeToAlarmIdMap = new ConcurrentSkipListMap<>();
    private AlarmService alarmService;

    public AlarmSchedular(AlarmService alarmService) {
        this.alarmService = alarmService;
        tickService = new TickService(this, alarmService);
    }


    public void scheduleAlarm(Alarm alarm) {
        List<UUID> alarms = new ArrayList<>();
        if (this.ringTimeToAlarmIdMap.containsKey(alarm.getNextRingTime())) {
            alarms = this.ringTimeToAlarmIdMap.get(alarm.getNextRingTime());
        }
        alarms.add(alarm.getId());
        this.ringTimeToAlarmIdMap.put(alarm.getNextRingTime(), alarms);
        tickService.handle();
    }

    public void unscheduleAlarm(Alarm alarm) {
        this.ringTimeToAlarmIdMap.get(alarm.getNextRingTime()).remove(alarm.getId());
        if (this.ringTimeToAlarmIdMap.get(alarm.getNextRingTime()).isEmpty()) {
            this.ringTimeToAlarmIdMap.remove(alarm.getNextRingTime());
        }
    }

    public void removeTimeAlarm(Integer time) {
        if (!this.ringTimeToAlarmIdMap.containsKey(time)) {
            return;
        }
        this.ringTimeToAlarmIdMap.remove(time);
    }

    public void updateScheduleAlarm(Alarm alarm, Alarm newAlarm) {
        unscheduleAlarm(alarm);
        scheduleAlarm(newAlarm);
    }

    public List<UUID> getLatestAlarm() {
        if (this.ringTimeToAlarmIdMap.isEmpty()) {
            return null;
        }
        List<UUID> alarmIds = this.ringTimeToAlarmIdMap.get(this.ringTimeToAlarmIdMap.firstKey());
        return alarmIds;

    }
}
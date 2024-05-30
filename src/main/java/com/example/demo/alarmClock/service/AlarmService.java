package com.example.demo.alarmClock.service;

import com.example.demo.alarmClock.enums.AlarmState;
import com.example.demo.alarmClock.model.Alarm;
import com.example.demo.alarmClock.repository.AlarmRepository;
import com.example.demo.alarmClock.scheduler.AlarmSchedular;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AlarmService {

    private AlarmSchedular alarmSchedular = new AlarmSchedular(this);
    private AlarmRepository alarmRepository = AlarmRepository.getInstance();
    private static final int DEFAULT_MAX_SNOOZE_COUNT = 3;

    public static void ringAlarm(Alarm alarm) {
        System.out.printf("Alarm ring for - %s%n", alarm);
    }

    public void addAlarm(Alarm alarm) {
        log.info("Adding Alarm - {}", alarm);
        alarmRepository.addAlarm(alarm);
        alarmSchedular.scheduleAlarm(alarm);
    }

    public void removeAlarm(Alarm alarm) {
        alarmSchedular.unscheduleAlarm(alarm);
        alarmRepository.deleteAlarm(alarm.getId());
    }

    public void updateAlarm(Alarm oldAlarm, Alarm newAlarm) {
        alarmSchedular.updateScheduleAlarm(oldAlarm, newAlarm);
        alarmRepository.deleteAlarm(oldAlarm.getId());
        alarmRepository.updateAlarm(newAlarm);
    }

    public void handlePostAlarm(Alarm alarm) {
        switch (alarm.getState()) {
            case SNOOZED:
                handleSnooze(alarm);
                break;
            case DISMISSED:
                handleDismiss(alarm);
                break;
        }

    }


    private void handleDismiss(Alarm alarm) {
        log.info("Handling Dismiss - {}", alarm);
        alarm.setSnoozed(false);
        alarm.setSnoozeCount(0);
        if (alarm.isRepeat()) {
            log.info("Alarm is repeat alarm");
            handleNextSchedule(alarm);
            return;
        } else {
            alarm.setState(AlarmState.DISABLED);
        }
        alarmRepository.updateAlarm(alarm);
    }

    private void handleSnooze(Alarm alarm) {
        log.info("Handling Snooze for - {}", alarm);
        alarm.setSnoozed(true);
        alarm.setSnoozeCount(alarm.getSnoozeCount() + 1);
        if (alarm.getSnoozeCount() >= DEFAULT_MAX_SNOOZE_COUNT) {
            log.info("Alarm reach its end state");
            handleDismiss(alarm);
        } else {
            alarm.setNextRingTime(alarm.getNextRingTime() + alarm.getSnoozeTime());
//                    * 60);
            alarmRepository.updateAlarm(alarm);
            alarmSchedular.scheduleAlarm(alarm);
        }

    }

    private void handleNextSchedule(Alarm alarm) {
        alarm.setState(AlarmState.NEW);
        alarm.setSnoozed(false);
        alarm.setSnoozeCount(0);
        // get the next schedule depending on Frequency and set the alarm

    }
}
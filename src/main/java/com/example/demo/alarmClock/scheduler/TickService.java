package com.example.demo.alarmClock.scheduler;


import com.example.demo.alarmClock.enums.AlarmState;
import com.example.demo.alarmClock.model.Alarm;
import com.example.demo.alarmClock.repository.AlarmRepository;
import com.example.demo.alarmClock.service.AlarmService;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
public class TickService {

    static ScheduledExecutorService executors = Executors.newScheduledThreadPool(1);
    private final AlarmRepository alarmRepository = AlarmRepository.getInstance();
    private final AlarmService alarmService;
    private final AlarmSchedular alarmSchedular;

    public TickService(AlarmSchedular alarmSchedular, AlarmService alarmService) {
        this.alarmSchedular = alarmSchedular;
        this.alarmService = alarmService;
    }

    public synchronized void handle() {
        try {
            handleAlarm();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Scheduling failed");
            // recursive
        }

    }

    public void handleAlarm() {
        List<UUID> alarmIdsToSchedule = alarmSchedular.getLatestAlarm();
        if (Objects.isNull(alarmIdsToSchedule)) {
            log.info("No alarms to schedule");
            return;
        }
        List<Alarm> alarmsToSchedule = alarmSchedular.getLatestAlarm()
                .parallelStream()
                .map(alarmRepository::getAlarm)
                .toList();

        executors.shutdownNow();
        executors = Executors.newScheduledThreadPool(1);
        executors.schedule(() -> {
            ExecutorService ringAlarmExecutor = Executors.newFixedThreadPool(alarmsToSchedule.size());
            alarmsToSchedule.forEach(alarm -> ringAlarmExecutor.submit(() -> {
                AlarmService.ringAlarm(alarm);
                alarm.setState(AlarmState.SNOOZED);
                alarmService.handlePostAlarm(alarm);
            }));
            ringAlarmExecutor.shutdown();
            alarmSchedular.removeTimeAlarm(alarmsToSchedule.get(0).getNextRingTime());
            handle();
        }, alarmsToSchedule.get(0).getNextRingTime() - Instant.now().getEpochSecond() , TimeUnit.SECONDS);
        executors.shutdown();


    }


}


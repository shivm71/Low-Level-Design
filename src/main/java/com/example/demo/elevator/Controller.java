package com.example.demo.elevator;

import jakarta.websocket.server.ServerEndpoint;
import lombok.Builder;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

@Slf4j
@Data
public class Controller {

    private final BlockingQueue<Request> requestQueue = new ArrayBlockingQueue<>(20);
    private List<Elevator> elevatorsList = new ArrayList<>();

    @SneakyThrows
    public Controller() {
        elevatorsList.sort(Comparator.comparingInt(Elevator::getId));
        Thread thread = new Thread(this::startPollingQueue);
        thread.start();
        thread.join();
    }

    @SneakyThrows
    private void startPollingQueue() {
        log.info("Polling requests....");
        while (true) {
            Request request = requestQueue.poll(10, TimeUnit.MILLISECONDS);
            if (request != null) {
                handleRequest(request);
            }

        }
    }

    private void handleRequest(Request request) {
        int destFloor = request.getRequestedFloor();
        for (Elevator elevator : elevatorsList) {
            if (elevator.getElevatorStatus() == Elevator.ElevatorStatus.IDLE) {
                elevator.goToFloor(destFloor);
                return;
            }
        }
    }

    public void addRequest(Request request) {
        requestQueue.add(request);
        // logic
    }

}

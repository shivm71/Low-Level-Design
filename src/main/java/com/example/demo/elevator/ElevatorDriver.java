package com.example.demo.elevator;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
public class ElevatorDriver {

    public static void main(String[] args) {
        Controller controller = new Controller();
        List<Elevator> elevators = new ArrayList<>();

        for (int i = 1; i <= 6; i += 1) {
            elevators.add(Elevator.builder().id(i).build());
        }
        log.info(elevators.toString());
        controller.setElevatorsList(elevators);
        Random random = new Random();
        int min = 0;
        int max = 6;
        for (int i = 1; i <= 10; i += 1) {
            int floor = random.nextInt(max - min) + min;
            Request request = Request.builder().requestedFloor(floor).build();
            controller.addRequest(request);
        }

    }

}

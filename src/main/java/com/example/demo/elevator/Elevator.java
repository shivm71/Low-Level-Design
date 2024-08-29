package com.example.demo.elevator;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

@Slf4j
@Data
@Builder
public class Elevator {

    private int id;
    @Builder.Default
    private ElevatorStatus elevatorStatus = ElevatorStatus.IDLE;
    private int currentFloor;
    private Set<Integer> allowedFloors = Set.of(0, 1, 2, 3, 4, 5, 6);

    public void goToFloor(int floor) {

        if (floor > this.currentFloor) {
            this.elevatorStatus = ElevatorStatus.MOVING_UP;
        } else {
            this.elevatorStatus = ElevatorStatus.MOVING_DOWN;
        }

        log.info("Lift - {}, going to floor - {} with status - {}.", id, floor, elevatorStatus);
        this.currentFloor = floor;
    }

    enum ElevatorStatus {
        IDLE, MOVING_UP, MOVING_DOWN
    }

}


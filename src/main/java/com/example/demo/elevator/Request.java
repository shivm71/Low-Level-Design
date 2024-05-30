package com.example.demo.elevator;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Data
@Slf4j
@Builder
public class Request {

    private Integer sourceFloor;
    private Integer requestedFloor;

    public boolean isOutFloorRequest() {
        return Objects.nonNull(sourceFloor) && (Objects.isNull(requestedFloor) || requestedFloor.equals(sourceFloor));
    }
}

package com.pcb.audy.test;

import com.pcb.audy.domain.pin.entity.Pin;
import java.util.UUID;

public interface PinTest extends CourseTest {
    UUID TEST_PIN_ID = UUID.randomUUID();
    String TEST_PIN_NAME = "pinName";
    String TEST_ORIGIN_NAME = "originName";
    Double TEST_LATITUDE = 1.0;
    Double TEST_LONGITUDE = 1.0;
    String TEST_ADDRESS = "address";
    Integer TEST_SEQUENCE = 1;

    Pin TEST_PIN =
            Pin.builder()
                    .pinId(TEST_PIN_ID)
                    .pinName(TEST_PIN_NAME)
                    .originName(TEST_ORIGIN_NAME)
                    .latitude(TEST_LATITUDE)
                    .longitude(TEST_LONGITUDE)
                    .address(TEST_ADDRESS)
                    .sequence(TEST_SEQUENCE)
                    .course(TEST_COURSE)
                    .build();
}

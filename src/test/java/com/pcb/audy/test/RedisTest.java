package com.pcb.audy.test;

import java.util.Set;

public interface RedisTest {
    String TEST_KEY = "key";
    String TEST_VALUE = "value";
    long TEST_EXPIRE_TIME = 10 * 60 * 1000L;
    Set<String> TEST_KEYS = Set.of(TEST_KEY);
}

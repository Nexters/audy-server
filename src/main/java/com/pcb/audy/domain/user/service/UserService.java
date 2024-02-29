package com.pcb.audy.domain.user.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pcb.audy.domain.user.dto.response.SocketUserGetRes;
import com.pcb.audy.domain.user.dto.response.UserGetRes;
import com.pcb.audy.domain.user.dto.response.UserGetResList;
import com.pcb.audy.domain.user.entity.User;
import com.pcb.audy.domain.user.repository.UserRepository;
import com.pcb.audy.global.redis.RedisProvider;
import com.pcb.audy.global.validator.UserValidator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final RedisProvider redisProvider;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    private final String COURSE_PREFIX = "socket:";

    public UserGetRes getUser(Long userId) {
        User user = userRepository.findByUserId(userId);
        UserValidator.validate(user);
        return UserServiceMapper.INSTANCE.toUserGetRes(user);
    }

    public UserGetResList getUsers(Long courseId) {
        String key = COURSE_PREFIX + courseId;

        List<SocketUserGetRes> users =
                redisProvider.getValues(key).stream()
                        .map(
                                user ->
                                        SocketUserGetRes.builder()
                                                .userId(objectMapper.convertValue(user, Long.class))
                                                .build())
                        .toList();

        return UserGetResList.builder().users(users).total(users.size()).build();
    }
}

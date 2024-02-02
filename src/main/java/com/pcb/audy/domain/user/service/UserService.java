package com.pcb.audy.domain.user.service;

import com.pcb.audy.domain.user.dto.response.UserGetRes;
import com.pcb.audy.domain.user.entity.User;
import com.pcb.audy.domain.user.repository.UserRepository;
import com.pcb.audy.global.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserGetRes getUser(Long userId) {
        User user = userRepository.findByUserId(userId);
        UserValidator.validate(user);
        return UserServiceMapper.INSTANCE.toUserGetRes(user);
    }
}

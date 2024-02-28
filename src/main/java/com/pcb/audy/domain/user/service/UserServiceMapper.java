package com.pcb.audy.domain.user.service;

import com.pcb.audy.domain.user.dto.response.SocketUserGetRes;
import com.pcb.audy.domain.user.dto.response.UserGetRes;
import com.pcb.audy.domain.user.entity.User;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserServiceMapper {
    UserServiceMapper INSTANCE = Mappers.getMapper(UserServiceMapper.class);

    UserGetRes toUserGetRes(User user);

    List<SocketUserGetRes> toUserGetResList(List<User> users);
}

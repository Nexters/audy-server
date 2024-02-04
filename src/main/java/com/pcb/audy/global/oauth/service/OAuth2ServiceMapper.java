package com.pcb.audy.global.oauth.service;

import com.pcb.audy.domain.user.entity.User;
import com.pcb.audy.global.oauth.dto.response.OAuth2Res;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OAuth2ServiceMapper {
    OAuth2ServiceMapper INSTANCE = Mappers.getMapper(OAuth2ServiceMapper.class);

    OAuth2Res toOAuth2Res(User user);
}

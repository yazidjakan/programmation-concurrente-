package com.ecomfsrwebflux.client_service.service.facade;

import com.ecomfsrwebflux.client_service.dto.UserDto;
import reactor.core.publisher.Mono;

public interface UserService extends AbstractService<UserDto, String> {
    Mono<UserDto> findByUsername(String username);
}
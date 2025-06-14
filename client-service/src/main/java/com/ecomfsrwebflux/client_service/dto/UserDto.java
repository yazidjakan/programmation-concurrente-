package com.ecomfsrwebflux.client_service.dto;

import java.util.List;
import java.util.Set;

public record UserDto(
        String id,
        String username,
        String email,
        String password,
        String roleId
) {
}

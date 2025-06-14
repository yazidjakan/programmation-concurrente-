package com.ecomfsrwebflux.client_service.transformer;

import com.ecomfsrwebflux.client_service.dto.UserDto;
import com.ecomfsrwebflux.client_service.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserTransformer extends AbstractTransformer<User,UserDto>{
    private final RoleTransformer roleTransformer;
    @Override
    public User toEntity(UserDto dto) {
        if(dto == null){
            return null;
        }else{
            User entity= new User();
            entity.setId(dto.id());
            entity.setUsername(dto.username());
            entity.setEmail(dto.email());
            entity.setPassword(dto.password());
            entity.setRoleId(dto.roleId());
            return entity;
        }
    }

    @Override
    public UserDto toDto(User entity) {
        if(entity == null){
            return null;
        }else{
            UserDto dto=new UserDto(
                    entity.getId(),
                    entity.getUsername(),
                    entity.getEmail(),
                    entity.getPassword(),
                    entity.getRoleId()
            );
            return dto;
        }
    }
}

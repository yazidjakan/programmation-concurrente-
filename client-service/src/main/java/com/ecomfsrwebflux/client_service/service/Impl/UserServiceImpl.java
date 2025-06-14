package com.ecomfsrwebflux.client_service.service.Impl;

import com.ecomfsrwebflux.client_service.dao.RoleDao;
import com.ecomfsrwebflux.client_service.dao.UserDao;
import com.ecomfsrwebflux.client_service.dto.RoleDto;
import com.ecomfsrwebflux.client_service.dto.UserDto;
import com.ecomfsrwebflux.client_service.entity.Role;
import com.ecomfsrwebflux.client_service.entity.User;
import com.ecomfsrwebflux.client_service.service.facade.UserService;
import com.ecomfsrwebflux.client_service.transformer.RoleTransformer;
import com.ecomfsrwebflux.client_service.transformer.UserTransformer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserTransformer userTransformer;
    private final UserDao userDao;
    private final RoleTransformer roleTransformer;
    private final RoleDao roleDao;

    @Override
    public Flux<UserDto> findAll() {
        log.info("Fetching all users");
        return userDao.findAll()
                .map(userTransformer::toDto)
                .switchIfEmpty(Flux.empty());
    }

    @Override
    public Mono<UserDto> save(UserDto dto) {
        return userDao.save(userTransformer.toEntity(dto))
                .map(userTransformer::toDto);
    }

    @Override
    public Flux<UserDto> save(List<UserDto> dtos) {
        return Flux.fromIterable(dtos)
                .parallel()
                .runOn(Schedulers.parallel())
                .flatMap(dto -> userDao.save(userTransformer.toEntity(dto)))
                .sequential()
                .map(userTransformer::toDto);
    }

    @Override
    public Mono<UserDto> findById(String id) {
        return userDao.findById(id)
                .map(userTransformer::toDto)
                .switchIfEmpty(Mono.error(new RuntimeException("User not found with ID: " + id)));
    }

    @Override
    public Mono<UserDto> findByUsername(String username) {
        log.info("Fetching user by username: {}", username);

        return userDao.findByUsername(username)
                .map(userTransformer::toDto)
                .switchIfEmpty(Mono.error(new RuntimeException("Unable to find a User with the given username : " + username)));
    }

    @Override
    public Mono<UserDto> updateById(String id, UserDto userDto) {
        log.info("Updating user with ID: {}", id);

        return userDao.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("User not found")))
                .flatMap(existingUser -> {
                    existingUser.setUsername(userDto.username());
                    existingUser.setPassword(userDto.password());
                    existingUser.setEmail(userDto.email());
                    existingUser.setRoleId(userDto.roleId());
                    return userDao.save(existingUser);
                })
                .map(savedUser -> new UserDto(
                        savedUser.getId(),
                        savedUser.getUsername(),
                        savedUser.getPassword(),
                        savedUser.getEmail(),
                        savedUser.getRoleId()
                ));
    }

    @Override
    public Mono<Void> deleteById(String id) {
        log.info("Deleting user with ID: {}", id);
        return userDao.deleteById(id);
    }

    public Flux<String> simulateConcurrentUserProcess() {
        return Flux.range(1, 5)
                .delayElements(Duration.ofSeconds(1))
                .map(i -> "Traitement concurrent utilisateur " + i)
                .doOnNext(log::info);
    }
}

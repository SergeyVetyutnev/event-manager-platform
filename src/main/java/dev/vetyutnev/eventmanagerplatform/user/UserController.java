package dev.vetyutnev.eventmanagerplatform.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping
    public ResponseEntity<UserDto> register(@Valid @RequestBody UserRegistrationDto request){
        log.info("HTTP запрос на регистрацию пользователя {}", request.login());

        var userDomain = userMapper.toDomainFromRegistration(request);

        var createdUser = userService.register(userDomain);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userMapper.toDto(createdUser));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getById(@PathVariable Long id){
        log.info("HTTP запрос на получение пользователя с id {}", id);

        var userDomain = userService.getById(id);

        return ResponseEntity.ok(userMapper.toDto(userDomain));
    }
}

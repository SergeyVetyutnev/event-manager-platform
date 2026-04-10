package dev.vetyutnev.eventmanagerplatform.user;

import dev.vetyutnev.eventmanagerplatform.location.exception.UserAlreadyExistsException;
import dev.vetyutnev.eventmanagerplatform.location.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;


    public User register(User userDomain) {
        log.info("Попытка регистрации пользователя с логином: {}", userDomain.login());

        if (userRepository.existsByLogin(userDomain.login())){
            throw new UserAlreadyExistsException(
                    "Пользователь с логином %s уже существует"
                            .formatted(userDomain.login()));
        }

        var securedUser = User.builder()
                .login(userDomain.login())
                .age(userDomain.age())
                .passwordHash(passwordEncoder.encode(userDomain.passwordHash()))
                .role(UserRole.USER)
                .build();

        var userEntity = userRepository.save(userMapper.toEntity(securedUser));
        log.info("Пользователь {} успешно зарегистрирован", userEntity.getLogin());

        return userMapper.toDomain(userEntity);
    }

    public User getById(Long id) {
        log.info("Запрос данных пользователя с id: {}", id);

        var userEntity = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(
                        "Пользователь с id " + id + " не найден"));

        return userMapper.toDomain(userEntity);
    }
}

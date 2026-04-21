package dev.vetyutnev.eventmanagerplatform.user;

import dev.vetyutnev.eventmanagerplatform.location.exception.UserAlreadyExistsException;
import dev.vetyutnev.eventmanagerplatform.location.exception.UserNotFoundException;
import dev.vetyutnev.eventmanagerplatform.security.JwtService;
import dev.vetyutnev.eventmanagerplatform.security.exception.InvalidCredentialException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;


    @Transactional
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
                .password(passwordEncoder.encode(userDomain.password()))
                .role(UserRole.USER)
                .build();

        var userEntity = userRepository.save(userMapper.toEntity(securedUser));
        log.info("Пользователь {} успешно зарегистрирован", userEntity.getLogin());

        return userMapper.toDomain(userEntity);
    }

    public String login(UserCredentials credentials){
        log.info("Попытка входа для пользователя: {}", credentials.login());

        UserEntity entity = userRepository.findByLogin(credentials.login())
                .orElseThrow(() -> new InvalidCredentialException("Неверный логин или пароль"));

        if (!passwordEncoder.matches(credentials.password(), entity.getPasswordHash())){
            log.warn("Неверный пароль для пользователя: {}", credentials.login());
            throw new InvalidCredentialException("Неверный логин или пароль");
        }

        User userDomain = userMapper.toDomain(entity);
        return jwtService.generateToken(userDomain);
    }

    @Transactional
    public void createAdmin(User adminDomain){
        if (userRepository.existsByLogin(adminDomain.login())){
            log.debug("Системный администратор '{}' уже существует, пропускаем создание.", adminDomain.login());
            return;
        }

        User admin = User.builder()
                .login(adminDomain.login())
                .password(passwordEncoder.encode(adminDomain.password()))
                .age(adminDomain.age())
                .role(UserRole.ADMIN)
                .build();

        userRepository.save(userMapper.toEntity(admin));
        log.info("Системный администратор '{}' успешно создан!", admin.login());
    }

    public User getById(Long id) {
        log.info("Запрос данных пользователя с id: {}", id);

        var userEntity = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(
                        "Пользователь с id " + id + " не найден"));

        return userMapper.toDomain(userEntity);
    }
}

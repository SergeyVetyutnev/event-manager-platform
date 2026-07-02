package dev.vetyutnev.eventmanagerplatform.security.service;

import dev.vetyutnev.eventmanagerplatform.security.config.AdminProperties;
import dev.vetyutnev.eventmanagerplatform.user.domain.User;
import dev.vetyutnev.eventmanagerplatform.user.service.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DefaultAdminInitializer {

    private final UserService userService;
    private final AdminProperties adminProperties;

    @PostConstruct
    public  void init(){
        log.info("Проверка наличия системного администратора...");

        User adminDomain = User.builder()
                .login(adminProperties.login())
                .password(adminProperties.password())
                .age(adminProperties.age())
                .build();

        userService.createAdmin(adminDomain);
    }
}

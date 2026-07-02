package dev.vetyutnev.eventmanagerplatform.event.service;

import dev.vetyutnev.eventmanagerplatform.event.exception.EventAccessDeniedException;
import dev.vetyutnev.eventmanagerplatform.common.security.TokenPayload;
import dev.vetyutnev.eventmanagerplatform.user.domain.UserRole;
import org.springframework.stereotype.Service;

@Service
public class EventPermissionService {

    public void verifyModificationAccess(Long eventOwnerId, TokenPayload currentUser){
        boolean isAdmin = UserRole.ADMIN.name().equals(currentUser.role());
        boolean isOwner = eventOwnerId.equals(currentUser.userId());

        if (!isAdmin && !isOwner){
            throw new EventAccessDeniedException(
                    "Только администратор или создатель может изменять это мероприятие");
        }
    }
}

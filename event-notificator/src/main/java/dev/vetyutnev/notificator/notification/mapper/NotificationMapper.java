package dev.vetyutnev.notificator.notification.mapper;

import dev.vetyutnev.notificator.notification.domain.Notification;
import dev.vetyutnev.notificator.notification.api.dto.NotificationResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface NotificationMapper {

    @Mapping(target = "notificationId", source = "id")
    NotificationResponseDto toDto(Notification domain);
}

package dev.vetyutnev.notificator.notification;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface NotificationMapper {

    @Mapping(target = "notificationId", source = "id")
    NotificationResponseDto toDto(Notification domain);
}

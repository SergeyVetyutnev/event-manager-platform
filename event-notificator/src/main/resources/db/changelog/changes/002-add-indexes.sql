--liquibase formatted sql

--changeset vetyutnev:002-add-indexes

-- 1 составной индекс для API (GET /notifications)
CREATE INDEX idx_notifications_user_id_is_read ON notifications(user_id, is_read);

-- 2 индекс для Foreign Key (ускорение JOIN при @EntityGraph)
CREATE INDEX idx_notifications_payload_id ON notifications(payload_id);

-- 3 индексы для работы NotificationCleanupScheduler (очистка старых данных)
CREATE INDEX idx_notifications_created_at ON notifications(created_at);
CREATE INDEX idx_payloads_occurred_at ON notification_event_payloads(occurred_at);

--rollback DROP INDEX idx_payloads_occurred_at;
--rollback DROP INDEX idx_notifications_created_at;
--rollback DROP INDEX idx_notifications_payload_id;
--rollback DROP INDEX idx_notifications_user_id_is_read;
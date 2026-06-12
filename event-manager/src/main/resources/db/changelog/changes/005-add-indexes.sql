--liquibase formatted sql

--changeset vetyutnev:005-add-indexes

-- 1 индексы для внешних ключей и частого поиска
CREATE INDEX idx_event_owner_id ON event(owner_id);
CREATE INDEX idx_event_location_id ON event(location_id);
CREATE INDEX idx_registration_user_id ON registration(user_id);

-- 2 составной индекс для сверхбыстрой работы EventStatusScheduler
CREATE INDEX idx_event_status_date ON event(status, date);

--rollback DROP INDEX idx_event_status_date;
--rollback DROP INDEX idx_registration_user_id;
--rollback DROP INDEX idx_event_location_id;
--rollback DROP INDEX idx_event_owner_id;
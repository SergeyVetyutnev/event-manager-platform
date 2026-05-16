--liquibase formatted sql

--changeset vetyutnev:002-location-description-and-check

ALTER TABLE location ADD COLUMN description VARCHAR(1000);

ALTER TABLE location ADD CONSTRAINT chk_location_capacity CHECK ( capacity >= 5 )


--rollback ALTER TABLE location DROP CONSTRAINT chk_location_capacity;
--rollback ALTER TABLE location DROP COLUMN description;
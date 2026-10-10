--liquibase formatted sql
--changeset andreibychek:003
ALTER TABLE users
    ADD COLUMN created_date TIMESTAMP NOT NULL,
    ADD COLUMN updated_date TIMESTAMP;

ALTER TABLE payment_cards
    ADD COLUMN created_date TIMESTAMP NOT NULL,
    ADD COLUMN updated_date TIMESTAMP;
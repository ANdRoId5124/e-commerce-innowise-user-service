--liquibase formatted sql
--changeset andreibychek:004

ALTER TABLE payment_cards
ADD COLUMN active BOOLEAN NOT NULL;
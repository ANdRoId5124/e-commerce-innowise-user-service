--liquibase formatted sql

--changeset andreibychek:005 endDelimiter:@@ rollbackEndDelimiter:@@
CREATE OR REPlACE FUNCTION check_is_user_has_max_number_of_cards()
RETURNS TRIGGER
LANGUAGE PLPGSQL
AS $$
BEGIN
IF( SELECT COUNT(*)
FROM payment_cards
WHERE user_id = NEW.user_id) >= 5
THEN
RAISE EXCEPTION 'User reached max number of cards';
END IF;
RETURN NEW;
END;
$$;


CREATE TRIGGER before_creating_card
BEFORE INSERT
ON payment_cards
FOR EACH ROW
EXECUTE FUNCTION check_is_user_has_max_number_of_cards()



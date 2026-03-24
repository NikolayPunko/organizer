
-- ============================================================
-- ТРИГГЕРЫ (7)
-- Триггеры используются для автоматического контроля данных
-- ============================================================


-- ------------------------------------------------------------
-- Функция аудита
-- ------------------------------------------------------------
CREATE OR REPLACE FUNCTION audit_trigger_function()
    RETURNS TRIGGER
    LANGUAGE plpgsql
AS $$
BEGIN

INSERT INTO audit_log(action, table_name, record_id)
VALUES (TG_OP, TG_TABLE_NAME, NEW.id);

RETURN NEW;

END;
$$;



-- ------------------------------------------------------------
-- Триггер аудита для weddings
-- ------------------------------------------------------------
CREATE TRIGGER trg_audit_weddings
    AFTER INSERT OR UPDATE OR DELETE
                    ON weddings
                        FOR EACH ROW
                        EXECUTE FUNCTION audit_trigger_function();



-- ------------------------------------------------------------
-- Триггер проверки положительной суммы платежа
-- ------------------------------------------------------------
CREATE OR REPLACE FUNCTION check_payment_amount()
    RETURNS TRIGGER
    LANGUAGE plpgsql
AS $$
BEGIN

    IF NEW.amount <= 0 THEN
        RAISE EXCEPTION 'Payment amount must be positive';
END IF;

RETURN NEW;

END;
$$;

CREATE TRIGGER trg_payment_check
    BEFORE INSERT ON payments
    FOR EACH ROW
    EXECUTE FUNCTION check_payment_amount();



-- -- ------------------------------------------------------------
-- -- Триггер автоматического времени изменения свадьбы
-- -- ------------------------------------------------------------
-- CREATE OR REPLACE FUNCTION update_wedding_timestamp()
--     RETURNS TRIGGER
--     LANGUAGE plpgsql
-- AS $$
-- BEGIN
--
--     NEW.updated_at = now();
-- RETURN NEW;
--
-- END;
-- $$;

-- CREATE TRIGGER trg_update_wedding_time
--     BEFORE UPDATE ON weddings
--     FOR EACH ROW
--     EXECUTE FUNCTION update_wedding_timestamp();



-- ------------------------------------------------------------
-- Триггер проверки даты встречи
-- ------------------------------------------------------------
CREATE OR REPLACE FUNCTION check_meeting_date()
    RETURNS TRIGGER
    LANGUAGE plpgsql
AS $$
BEGIN

    IF NEW.meeting_date < now() THEN
        RAISE NOTICE 'Meeting date is in the past';
END IF;

RETURN NEW;

END;
$$;

CREATE TRIGGER trg_meeting_check
    BEFORE INSERT ON meetings
    FOR EACH ROW
    EXECUTE FUNCTION check_meeting_date();



-- ------------------------------------------------------------
-- Триггер автоматического статуса задачи
-- ------------------------------------------------------------
CREATE OR REPLACE FUNCTION auto_task_status()
    RETURNS TRIGGER
    LANGUAGE plpgsql
AS $$
BEGIN

    IF NEW.status IS NULL THEN
        NEW.status := 'NEW';
END IF;

RETURN NEW;

END;
$$;

CREATE TRIGGER trg_task_status
    BEFORE INSERT ON tasks
    FOR EACH ROW
    EXECUTE FUNCTION auto_task_status();



-- ------------------------------------------------------------
-- Триггер предотвращения дублирования команды
-- ------------------------------------------------------------
CREATE OR REPLACE FUNCTION prevent_duplicate_team()
    RETURNS TRIGGER
    LANGUAGE plpgsql
AS $$
BEGIN

    IF EXISTS(
        SELECT 1 FROM wedding_team
        WHERE wedding_id = NEW.wedding_id
          AND member_id = NEW.member_id
    ) THEN
        RAISE EXCEPTION 'Team member already assigned';
END IF;

RETURN NEW;

END;
$$;

CREATE TRIGGER trg_team_duplicate
    BEFORE INSERT ON wedding_team
    FOR EACH ROW
    EXECUTE FUNCTION prevent_duplicate_team();
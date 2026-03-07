
-- ============================================================
-- ПРОЦЕДУРЫ (7)
-- Процедуры используются для выполнения бизнес-операций
-- внутри системы.
-- ============================================================


-- ------------------------------------------------------------
-- Процедура sp_create_wedding
-- Создает новый свадебный проект
-- ------------------------------------------------------------
CREATE OR REPLACE PROCEDURE sp_create_wedding(
    p_client_id BIGINT,
    p_wedding_date DATE,
    p_budget NUMERIC,
    p_user BIGINT
)
    LANGUAGE plpgsql
AS $$
BEGIN

INSERT INTO weddings(client_id, wedding_date, budget_total, created_by)
VALUES (p_client_id, p_wedding_date, p_budget, p_user);

END;
$$;



-- ------------------------------------------------------------
-- Процедура sp_close_wedding
-- Закрывает свадебный проект
-- ------------------------------------------------------------
CREATE OR REPLACE PROCEDURE sp_close_wedding(p_wedding_id BIGINT)
    LANGUAGE plpgsql
AS $$
BEGIN

UPDATE weddings
SET status = 'COMPLETED'
WHERE id = p_wedding_id;

END;
$$;



-- ------------------------------------------------------------
-- Процедура sp_add_vendor_contract
-- Добавляет подрядчика на свадьбу
-- ------------------------------------------------------------
CREATE OR REPLACE PROCEDURE sp_add_vendor_contract(
    p_wedding_id BIGINT,
    p_service_id BIGINT,
    p_price NUMERIC
)
    LANGUAGE plpgsql
AS $$
BEGIN

INSERT INTO contracts(wedding_id, service_id, agreed_price, contract_date)
VALUES (p_wedding_id, p_service_id, p_price, CURRENT_DATE);

END;
$$;



-- ------------------------------------------------------------
-- Процедура sp_register_payment
-- Регистрирует платеж
-- ------------------------------------------------------------
CREATE OR REPLACE PROCEDURE sp_register_payment(
    p_wedding_id BIGINT,
    p_contract_id BIGINT,
    p_amount NUMERIC,
    p_type TEXT
)
    LANGUAGE plpgsql
AS $$
BEGIN

INSERT INTO payments(wedding_id, contract_id, amount, payment_type)
VALUES (p_wedding_id, p_contract_id, p_amount, p_type);

END;
$$;



-- ------------------------------------------------------------
-- Процедура sp_assign_task
-- Назначает задачу сотруднику
-- ------------------------------------------------------------
CREATE OR REPLACE PROCEDURE sp_assign_task(
    p_wedding_id BIGINT,
    p_title TEXT,
    p_member_id BIGINT,
    p_due DATE
)
    LANGUAGE plpgsql
AS $$
BEGIN

INSERT INTO tasks(wedding_id,title,assigned_to,due_date)
VALUES (p_wedding_id,p_title,p_member_id,p_due);

END;
$$;



-- ------------------------------------------------------------
-- Процедура sp_complete_task
-- Завершает задачу
-- ------------------------------------------------------------
CREATE OR REPLACE PROCEDURE sp_complete_task(p_task_id BIGINT)
    LANGUAGE plpgsql
AS $$
BEGIN

UPDATE tasks
SET status='DONE'
WHERE id=p_task_id;

END;
$$;



-- ------------------------------------------------------------
-- Процедура sp_generate_initial_tasks
-- Создает стандартные задачи для новой свадьбы
-- ------------------------------------------------------------
CREATE OR REPLACE PROCEDURE sp_generate_initial_tasks(p_wedding_id BIGINT)
    LANGUAGE plpgsql
AS $$
BEGIN

INSERT INTO tasks(wedding_id,title) VALUES
                                        (p_wedding_id,'Find wedding venue'),
                                        (p_wedding_id,'Select photographer'),
                                        (p_wedding_id,'Choose decoration'),
                                        (p_wedding_id,'Plan wedding program');

END;
$$;

-- ============================================================
-- ФУНКЦИИ, ПРОЦЕДУРЫ И ТРИГГЕРЫ
-- БД: Управление работой свадебного организатора
-- ============================================================


-- ============================================================
-- ФУНКЦИИ (7)
-- Функции используются для вычислений и формирования отчетных
-- данных внутри базы данных.
-- ============================================================


-- ------------------------------------------------------------
-- Функция fn_wedding_spent
-- Возвращает общую сумму расходов по свадьбе
-- ------------------------------------------------------------
CREATE OR REPLACE FUNCTION fn_wedding_spent(p_wedding_id BIGINT)
    RETURNS NUMERIC
    LANGUAGE plpgsql
AS $$
DECLARE
total NUMERIC;
BEGIN
SELECT COALESCE(SUM(amount),0)
INTO total
FROM payments
WHERE wedding_id = p_wedding_id
  AND payment_type = 'OUT';

RETURN total;
END;
$$;



-- ------------------------------------------------------------
-- Функция fn_wedding_income
-- Возвращает сумму поступлений от клиента
-- ------------------------------------------------------------
CREATE OR REPLACE FUNCTION fn_wedding_income(p_wedding_id BIGINT)
    RETURNS NUMERIC
    LANGUAGE plpgsql
AS $$
DECLARE
total NUMERIC;
BEGIN
SELECT COALESCE(SUM(amount),0)
INTO total
FROM payments
WHERE wedding_id = p_wedding_id
  AND payment_type = 'IN';

RETURN total;
END;
$$;



-- ------------------------------------------------------------
-- Функция fn_wedding_balance
-- Возвращает текущий баланс свадьбы
-- ------------------------------------------------------------
CREATE OR REPLACE FUNCTION fn_wedding_balance(p_wedding_id BIGINT)
    RETURNS NUMERIC
    LANGUAGE plpgsql
AS $$
DECLARE
income NUMERIC;
    spent NUMERIC;
BEGIN

    income := fn_wedding_income(p_wedding_id);
    spent := fn_wedding_spent(p_wedding_id);

RETURN income - spent;

END;
$$;



-- ------------------------------------------------------------
-- Функция fn_overdue_tasks_count
-- Подсчитывает количество просроченных задач
-- ------------------------------------------------------------
CREATE OR REPLACE FUNCTION fn_overdue_tasks_count(p_wedding_id BIGINT)
    RETURNS INTEGER
    LANGUAGE plpgsql
AS $$
DECLARE
total INTEGER;
BEGIN

SELECT COUNT(*)
INTO total
FROM tasks
WHERE wedding_id = p_wedding_id
  AND status <> 'DONE'
  AND due_date < CURRENT_DATE;

RETURN total;

END;
$$;



-- ------------------------------------------------------------
-- Функция fn_vendor_total
-- Считает сумму выплат конкретному подрядчику
-- ------------------------------------------------------------
CREATE OR REPLACE FUNCTION fn_vendor_total(p_vendor_id BIGINT)
    RETURNS NUMERIC
    LANGUAGE plpgsql
AS $$
DECLARE
total NUMERIC;
BEGIN

SELECT COALESCE(SUM(c.agreed_price),0)
INTO total
FROM contracts c
         JOIN services s ON s.id = c.service_id
WHERE s.vendor_id = p_vendor_id;

RETURN total;

END;
$$;



-- ------------------------------------------------------------
-- Функция fn_team_workload
-- Показывает количество активных задач сотрудника
-- ------------------------------------------------------------
CREATE OR REPLACE FUNCTION fn_team_workload(p_member_id BIGINT)
    RETURNS INTEGER
    LANGUAGE plpgsql
AS $$
DECLARE
total INTEGER;
BEGIN

SELECT COUNT(*)
INTO total
FROM tasks
WHERE assigned_to = p_member_id
  AND status <> 'DONE';

RETURN total;

END;
$$;



-- ------------------------------------------------------------
-- Функция fn_wedding_task_count
-- Возвращает общее количество задач свадьбы
-- ------------------------------------------------------------
CREATE OR REPLACE FUNCTION fn_wedding_task_count(p_wedding_id BIGINT)
    RETURNS INTEGER
    LANGUAGE plpgsql
AS $$
DECLARE
total INTEGER;
BEGIN

SELECT COUNT(*)
INTO total
FROM tasks
WHERE wedding_id = p_wedding_id;

RETURN total;

END;
$$;

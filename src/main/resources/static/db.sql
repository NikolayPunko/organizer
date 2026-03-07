-- ============================================================
-- База данных: Управление работой свадебного организатора
-- PostgreSQL
--
-- Содержимое:
-- 1. Создание таблиц (15)
-- 2. Ограничения и связи
-- 3. Тестовые данные
-- ============================================================

BEGIN;

-- ============================================================
-- 1) Таблицы доступа: users, roles, user_roles
-- Таблицы используются для реализации многопользовательского
-- режима и разграничения прав доступа в приложении.
-- ============================================================

-- ------------------------------------------------------------
-- Таблица users
-- Хранит учетные записи пользователей системы.
-- ------------------------------------------------------------
CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       email TEXT NOT NULL UNIQUE,
                       password_hash TEXT NOT NULL,
                       full_name TEXT NOT NULL,
                       is_active BOOLEAN NOT NULL DEFAULT TRUE,
                       created_at TIMESTAMP NOT NULL DEFAULT now()
);

-- ------------------------------------------------------------
-- Таблица roles
-- Справочник ролей пользователей.
-- ------------------------------------------------------------
CREATE TABLE roles (
                       id BIGSERIAL PRIMARY KEY,
                       role_name TEXT NOT NULL UNIQUE
);

-- ------------------------------------------------------------
-- Таблица user_roles
-- Связь пользователей и ролей.
-- ------------------------------------------------------------
CREATE TABLE user_roles (
                            user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
                            role_id BIGINT REFERENCES roles(id) ON DELETE CASCADE,
                            PRIMARY KEY (user_id, role_id)
);

-- ============================================================
-- 2) Таблицы клиентов и свадеб
-- ============================================================

-- ------------------------------------------------------------
-- Таблица clients
-- Хранит данные клиентов (пары).
-- ------------------------------------------------------------
CREATE TABLE clients (
                         id BIGSERIAL PRIMARY KEY,
                         name1 TEXT NOT NULL,
                         name2 TEXT,
                         phone TEXT,
                         email TEXT,
                         created_at TIMESTAMP DEFAULT now()
);

-- ------------------------------------------------------------
-- Таблица weddings
-- Основная таблица свадебных проектов.
-- ------------------------------------------------------------
CREATE TABLE weddings (
                          id BIGSERIAL PRIMARY KEY,
                          client_id BIGINT REFERENCES clients(id),
                          wedding_date DATE NOT NULL,
                          status TEXT NOT NULL DEFAULT 'PLANNING',
                          budget_total NUMERIC(12,2) DEFAULT 0,
                          created_by BIGINT REFERENCES users(id),
                          created_at TIMESTAMP DEFAULT now()
);

-- ============================================================
-- 3) Таблицы команды
-- ============================================================

-- ------------------------------------------------------------
-- Таблица team_members
-- Сотрудники свадебного агентства.
-- ------------------------------------------------------------
CREATE TABLE team_members (
                              id BIGSERIAL PRIMARY KEY,
                              full_name TEXT NOT NULL,
                              position TEXT,
                              phone TEXT,
                              email TEXT
);

-- ------------------------------------------------------------
-- Таблица wedding_team
-- Назначение сотрудников на свадьбу.
-- ------------------------------------------------------------
CREATE TABLE wedding_team (
                              id BIGSERIAL PRIMARY KEY,
                              wedding_id BIGINT REFERENCES weddings(id) ON DELETE CASCADE,
                              member_id BIGINT REFERENCES team_members(id),
                              role TEXT
);

-- ============================================================
-- 4) Таблицы подрядчиков и услуг
-- ============================================================

-- ------------------------------------------------------------
-- Таблица vendors
-- Подрядчики (фотографы, ведущие, декораторы и т.д.)
-- ------------------------------------------------------------
CREATE TABLE vendors (
                         id BIGSERIAL PRIMARY KEY,
                         name TEXT NOT NULL,
                         service_type TEXT,
                         phone TEXT,
                         email TEXT
);

-- ------------------------------------------------------------
-- Таблица services
-- Услуги подрядчиков.
-- ------------------------------------------------------------
CREATE TABLE services (
                          id BIGSERIAL PRIMARY KEY,
                          vendor_id BIGINT REFERENCES vendors(id),
                          service_name TEXT NOT NULL,
                          base_price NUMERIC(12,2)
);

-- ------------------------------------------------------------
-- Таблица contracts
-- Договор на услугу для конкретной свадьбы.
-- ------------------------------------------------------------
CREATE TABLE contracts (
                           id BIGSERIAL PRIMARY KEY,
                           wedding_id BIGINT REFERENCES weddings(id),
                           service_id BIGINT REFERENCES services(id),
                           agreed_price NUMERIC(12,2),
                           contract_date DATE,
                           status TEXT DEFAULT 'PENDING'
);

-- ============================================================
-- 5) Таблицы планирования
-- ============================================================

-- ------------------------------------------------------------
-- Таблица tasks
-- Задачи по подготовке свадьбы.
-- ------------------------------------------------------------
CREATE TABLE tasks (
                       id BIGSERIAL PRIMARY KEY,
                       wedding_id BIGINT REFERENCES weddings(id),
                       title TEXT NOT NULL,
                       description TEXT,
                       assigned_to BIGINT REFERENCES team_members(id),
                       due_date DATE,
                       status TEXT DEFAULT 'NEW'
);

-- ------------------------------------------------------------
-- Таблица meetings
-- Встречи с клиентами или подрядчиками.
-- ------------------------------------------------------------
CREATE TABLE meetings (
                          id BIGSERIAL PRIMARY KEY,
                          wedding_id BIGINT REFERENCES weddings(id),
                          meeting_date TIMESTAMP,
                          location TEXT,
                          notes TEXT
);

-- ============================================================
-- 6) Финансы
-- ============================================================

-- ------------------------------------------------------------
-- Таблица payments
-- Финансовые операции (приход/расход).
-- ------------------------------------------------------------
CREATE TABLE payments (
                          id BIGSERIAL PRIMARY KEY,
                          wedding_id BIGINT REFERENCES weddings(id),
                          contract_id BIGINT REFERENCES contracts(id),
                          amount NUMERIC(12,2) NOT NULL,
                          payment_type TEXT NOT NULL,
                          payment_date DATE DEFAULT CURRENT_DATE,
                          notes TEXT
);

-- ============================================================
-- 7) Таблицы журналирования и обслуживания
-- ============================================================

-- ------------------------------------------------------------
-- Таблица audit_log
-- Журнал действий пользователей.
-- ------------------------------------------------------------
CREATE TABLE audit_log (
                           id BIGSERIAL PRIMARY KEY,
                           action TEXT,
                           table_name TEXT,
                           record_id BIGINT,
                           action_time TIMESTAMP DEFAULT now()
);

-- ------------------------------------------------------------
-- Таблица backup_jobs
-- Журнал резервного копирования базы данных.
-- ------------------------------------------------------------
CREATE TABLE backup_jobs (
                             id BIGSERIAL PRIMARY KEY,
                             backup_time TIMESTAMP DEFAULT now(),
                             status TEXT,
                             file_path TEXT
);

-- ============================================================
-- Тестовые данные
-- ============================================================

-- Роли
INSERT INTO roles (role_name) VALUES
                                  ('ADMIN'),
                                  ('ORGANIZER'),
                                  ('ASSISTANT');

-- Пользователи
INSERT INTO users (email, password_hash, full_name) VALUES
                                                        ('admin@test.com','hash1','Admin User'),
                                                        ('organizer@test.com','hash2','Main Organizer'),
                                                        ('assistant@test.com','hash3','Assistant User');

-- Назначение ролей
INSERT INTO user_roles VALUES
                           (1,1),
                           (2,2),
                           (3,3);

-- Клиенты
INSERT INTO clients (name1,name2,phone,email) VALUES
                                                  ('Ivan Ivanov','Anna Petrova','+123456789','ivan@test.com'),
                                                  ('Sergey Smirnov','Olga Volkova','+987654321','sergey@test.com');

-- Свадьбы
INSERT INTO weddings (client_id,wedding_date,budget_total,created_by) VALUES
                                                                          (1,'2026-07-10',20000,2),
                                                                          (2,'2026-08-15',30000,2);

-- Команда
INSERT INTO team_members (full_name,position,phone,email) VALUES
                                                              ('Alex Petrov','Organizer','111111','alex@test.com'),
                                                              ('Maria Ivanova','Assistant','222222','maria@test.com');

-- Назначение команды
INSERT INTO wedding_team (wedding_id,member_id,role) VALUES
                                                         (1,1,'Organizer'),
                                                         (1,2,'Assistant'),
                                                         (2,1,'Organizer');

-- Подрядчики
INSERT INTO vendors (name,service_type,phone,email) VALUES
                                                        ('Best Photo','Photography','111','photo@test.com'),
                                                        ('Top Decor','Decoration','222','decor@test.com');

-- Услуги
INSERT INTO services (vendor_id,service_name,base_price) VALUES
                                                             (1,'Wedding photography',3000),
                                                             (2,'Hall decoration',5000);

-- Договоры
INSERT INTO contracts (wedding_id,service_id,agreed_price,contract_date,status) VALUES
                                                                                    (1,1,2800,'2026-06-01','SIGNED'),
                                                                                    (1,2,4500,'2026-06-05','SIGNED');

-- Задачи
INSERT INTO tasks (wedding_id,title,assigned_to,due_date,status) VALUES
                                                                     (1,'Book restaurant',1,'2026-05-01','IN_PROGRESS'),
                                                                     (1,'Confirm photographer',2,'2026-05-10','NEW');

-- Встречи
INSERT INTO meetings (wedding_id,meeting_date,location,notes) VALUES
    (1,'2026-04-01 18:00','Office','Initial planning meeting');

-- Платежи
INSERT INTO payments (wedding_id,contract_id,amount,payment_type,notes) VALUES
                                                                            (1,1,1000,'OUT','Advance payment for photographer'),
                                                                            (1,2,2000,'OUT','Decoration prepayment');

COMMIT;
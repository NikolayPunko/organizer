package com.organizer.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class ReportRepository {

    private final NamedParameterJdbcTemplate jdbc;

    // ------------------------------------------------------------
    // 1. Финансовая сводка по свадьбам
    // Использует функции БД:
    // fn_wedding_income, fn_wedding_spent, fn_wedding_balance
    // ------------------------------------------------------------
    public List<Map<String, Object>> weddingFinance() {
        String sql = """
                SELECT
                    w.id AS wedding_id,
                    c.name1 AS client_name_1,
                    c.name2 AS client_name_2,
                    w.wedding_date,
                    w.status,
                    w.budget_total,
                    fn_wedding_income(w.id) AS income_total,
                    fn_wedding_spent(w.id) AS spent_total,
                    fn_wedding_balance(w.id) AS balance_total
                FROM weddings w
                JOIN clients c ON c.id = w.client_id
                ORDER BY w.wedding_date
                """;

        return jdbc.queryForList(sql, Map.of());
    }

    // ------------------------------------------------------------
    // 2. Просроченные задачи
    // Использует функцию БД: fn_overdue_tasks_count
    // ------------------------------------------------------------
    public List<Map<String, Object>> overdueTasks() {
        String sql = """
                SELECT
                    w.id AS wedding_id,
                    c.name1 AS client_name_1,
                    c.name2 AS client_name_2,
                    fn_overdue_tasks_count(w.id) AS overdue_count
                FROM weddings w
                JOIN clients c ON c.id = w.client_id
                WHERE fn_overdue_tasks_count(w.id) > 0
                ORDER BY overdue_count DESC, w.id
                """;

        return jdbc.queryForList(sql, Map.of());
    }

    // ------------------------------------------------------------
    // 3. Нагрузка сотрудников
    // Использует функцию БД: fn_team_workload
    // ------------------------------------------------------------
    public List<Map<String, Object>> teamWorkload() {
        String sql = """
                SELECT
                    tm.id AS member_id,
                    tm.full_name,
                    tm.position,
                    fn_team_workload(tm.id) AS active_tasks
                FROM team_members tm
                ORDER BY active_tasks DESC, tm.full_name
                """;

        return jdbc.queryForList(sql, Map.of());
    }

    // ------------------------------------------------------------
    // 4. Подрядчики и общая стоимость их договоров
    // Использует функцию БД: fn_vendor_total
    // ------------------------------------------------------------
    public List<Map<String, Object>> vendorTotals() {
        String sql = """
                SELECT
                    v.id AS vendor_id,
                    v.name AS vendor_name,
                    v.service_type,
                    fn_vendor_total(v.id) AS total_contract_sum
                FROM vendors v
                ORDER BY total_contract_sum DESC, v.name
                """;

        return jdbc.queryForList(sql, Map.of());
    }

    // ------------------------------------------------------------
    // 5. Статистика свадеб по статусам
    // ------------------------------------------------------------
    public List<Map<String, Object>> weddingsByStatus() {
        String sql = """
                SELECT
                    w.status,
                    COUNT(*) AS weddings_count,
                    COALESCE(SUM(w.budget_total), 0) AS total_budget,
                    COALESCE(AVG(w.budget_total), 0) AS avg_budget
                FROM weddings w
                GROUP BY w.status
                ORDER BY weddings_count DESC, w.status
                """;

        return jdbc.queryForList(sql, Map.of());
    }

    // ------------------------------------------------------------
    // 6. Расходы по подрядчикам за период
    // ------------------------------------------------------------
    public List<Map<String, Object>> vendorPayments(LocalDate start, LocalDate end) {
        String sql = """
                SELECT
                    v.id AS vendor_id,
                    v.name AS vendor_name,
                    v.service_type,
                    COUNT(p.id) AS payments_count,
                    COALESCE(SUM(p.amount), 0) AS total_paid
                FROM payments p
                JOIN contracts c ON c.id = p.contract_id
                JOIN services s ON s.id = c.service_id
                JOIN vendors v ON v.id = s.vendor_id
                WHERE p.payment_type = 'OUT'
                  AND p.payment_date BETWEEN :start AND :end
                GROUP BY v.id, v.name, v.service_type
                ORDER BY total_paid DESC, v.name
                """;

        return jdbc.queryForList(sql, Map.of(
                "start", start,
                "end", end
        ));
    }

    // ------------------------------------------------------------
    // 7. Платежи по типам за период
    // ------------------------------------------------------------
    public List<Map<String, Object>> paymentsByType(LocalDate start, LocalDate end) {
        String sql = """
                SELECT
                    p.payment_type,
                    COUNT(*) AS payments_count,
                    COALESCE(SUM(p.amount), 0) AS total_amount
                FROM payments p
                WHERE p.payment_date BETWEEN :start AND :end
                GROUP BY p.payment_type
                ORDER BY total_amount DESC, p.payment_type
                """;

        return jdbc.queryForList(sql, Map.of(
                "start", start,
                "end", end
        ));
    }

    // ------------------------------------------------------------
    // 8. Контракты по статусам
    // ------------------------------------------------------------
    public List<Map<String, Object>> contractsByStatus() {
        String sql = """
                SELECT
                    c.status,
                    COUNT(*) AS contracts_count,
                    COALESCE(SUM(c.agreed_price), 0) AS total_sum
                FROM contracts c
                GROUP BY c.status
                ORDER BY contracts_count DESC, c.status
                """;

        return jdbc.queryForList(sql, Map.of());
    }

    // ------------------------------------------------------------
    // 9. Встречи по свадьбам за период
    // ------------------------------------------------------------
    public List<Map<String, Object>> meetingsByPeriod(LocalDate start, LocalDate end) {
        String sql = """
                SELECT
                    w.id AS wedding_id,
                    cl.name1 AS client_name_1,
                    cl.name2 AS client_name_2,
                    COUNT(m.id) AS meetings_count
                FROM weddings w
                JOIN clients cl ON cl.id = w.client_id
                LEFT JOIN meetings m
                    ON m.wedding_id = w.id
                   AND m.meeting_date::date BETWEEN :start AND :end
                GROUP BY w.id, cl.name1, cl.name2
                ORDER BY meetings_count DESC, w.id
                """;

        return jdbc.queryForList(sql, Map.of(
                "start", start,
                "end", end
        ));
    }

    // ------------------------------------------------------------
    // 10. Сводка журнала аудита за период
    // ------------------------------------------------------------
    public List<Map<String, Object>> auditSummary(LocalDate start, LocalDate end) {
        String sql = """
                SELECT
                    a.table_name,
                    a.action,
                    COUNT(*) AS actions_count,
                    MIN(a.action_time) AS first_action_at,
                    MAX(a.action_time) AS last_action_at
                FROM audit_log a
                WHERE a.action_time::date BETWEEN :start AND :end
                GROUP BY a.table_name, a.action
                ORDER BY a.table_name, a.action
                """;

        return jdbc.queryForList(sql, Map.of(
                "start", start,
                "end", end
        ));
    }
}
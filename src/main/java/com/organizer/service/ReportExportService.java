package com.organizer.service;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.organizer.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportExportService {

    private final ReportRepository repo;

    public byte[] exportCsv(String reportKey, Map<String, String> params) {
        List<Map<String, Object>> data = loadReport(reportKey, params);
        String csv = toCsv(data);
        return csv.getBytes(StandardCharsets.UTF_8);
    }

    public byte[] exportXlsx(String reportKey, Map<String, String> params) {
        List<Map<String, Object>> data = loadReport(reportKey, params);
        return toXlsx(reportKey, data);
    }

    public byte[] exportPdf(String reportKey, Map<String, String> params) {
        List<Map<String, Object>> data = loadReport(reportKey, params);
        return toPdf(reportKey, data);
    }

    private List<Map<String, Object>> loadReport(String reportKey, Map<String, String> params) {
        return switch (reportKey) {
            case "wedding-finance" -> repo.weddingFinance();
            case "overdue-tasks" -> repo.overdueTasks();
            case "team-workload" -> repo.teamWorkload();
            case "vendor-totals" -> repo.vendorTotals();
            case "weddings-by-status" -> repo.weddingsByStatus();

            case "vendor-payments" -> {
                LocalDate start = dateParam(params, "start");
                LocalDate end = dateParam(params, "end");
                yield repo.vendorPayments(start, end);
            }

            case "payments-by-type" -> {
                LocalDate start = dateParam(params, "start");
                LocalDate end = dateParam(params, "end");
                yield repo.paymentsByType(start, end);
            }

            case "contracts-by-status" -> repo.contractsByStatus();

            case "meetings-by-period" -> {
                LocalDate start = dateParam(params, "start");
                LocalDate end = dateParam(params, "end");
                yield repo.meetingsByPeriod(start, end);
            }

            case "audit-summary" -> {
                LocalDate start = dateParam(params, "start");
                LocalDate end = dateParam(params, "end");
                yield repo.auditSummary(start, end);
            }

            default -> throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Unknown reportKey: " + reportKey
            );
        };
    }

    private String toCsv(List<Map<String, Object>> data) {
        if (data == null || data.isEmpty()) {
            return "";
        }

        List<String> headers = new ArrayList<>(data.get(0).keySet());
        StringBuilder sb = new StringBuilder();

        sb.append(String.join(",", headers)).append("\n");

        for (Map<String, Object> row : data) {
            List<String> values = new ArrayList<>();
            for (String header : headers) {
                values.add(escapeCsv(row.get(header)));
            }
            sb.append(String.join(",", values)).append("\n");
        }

        return sb.toString();
    }

    private String escapeCsv(Object value) {
        if (value == null) {
            return "";
        }

        String s = String.valueOf(value);
        boolean needQuotes = s.contains(",") || s.contains("\"") || s.contains("\n") || s.contains("\r");
        s = s.replace("\"", "\"\"");

        return needQuotes ? "\"" + s + "\"" : s;
    }

    private byte[] toXlsx(String title, List<Map<String, Object>> data) {
        try (Workbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet("report");

            if (data == null || data.isEmpty()) {
                Row row = sheet.createRow(0);
                row.createCell(0).setCellValue("No data");
                return workbookToBytes(wb);
            }

            List<String> headers = new ArrayList<>(data.get(0).keySet());

            CellStyle headerStyle = wb.createCellStyle();
            org.apache.poi.ss.usermodel.Font headerFont = wb.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.size(); i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers.get(i));
                cell.setCellStyle(headerStyle);
            }

            for (int r = 0; r < data.size(); r++) {
                Row row = sheet.createRow(r + 1);
                Map<String, Object> map = data.get(r);

                for (int c = 0; c < headers.size(); c++) {
                    Object value = map.get(headers.get(c));
                    Cell cell = row.createCell(c);
                    cell.setCellValue(value == null ? "" : String.valueOf(value));
                }
            }

            for (int i = 0; i < headers.size(); i++) {
                sheet.autoSizeColumn(i);
            }

            return workbookToBytes(wb);

        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "XLSX export failed: " + e.getMessage()
            );
        }
    }

    private byte[] workbookToBytes(Workbook wb) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        wb.write(out);
        return out.toByteArray();
    }

    private byte[] toPdf(String title, List<Map<String, Object>> data) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            Document document = new Document(PageSize.A4.rotate(), 20, 20, 20, 20);
            PdfWriter.getInstance(document, out);

            document.open();

            Font titleFont = new Font(Font.HELVETICA, 16, Font.BOLD);
            Font headerFont = new Font(Font.HELVETICA, 10, Font.BOLD);
            Font cellFont = new Font(Font.HELVETICA, 9, Font.NORMAL);

            document.add(new Paragraph("Report: " + title, titleFont));
            document.add(new Paragraph("Generated: " + new Date()));
            document.add(new Paragraph(" "));

            if (data == null || data.isEmpty()) {
                document.add(new Paragraph("No data"));
                document.close();
                return out.toByteArray();
            }

            List<String> headers = new ArrayList<>(data.get(0).keySet());

            PdfPTable table = new PdfPTable(headers.size());
            table.setWidthPercentage(100);

            for (String header : headers) {
                table.addCell(new Phrase(header, headerFont));
            }

            for (Map<String, Object> row : data) {
                for (String header : headers) {
                    Object value = row.get(header);
                    table.addCell(new Phrase(value == null ? "" : String.valueOf(value), cellFont));
                }
            }

            document.add(table);
            document.close();

            return out.toByteArray();

        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "PDF export failed: " + e.getMessage()
            );
        }
    }

    private LocalDate dateParam(Map<String, String> params, String key) {
        try {
            String value = params.get(key);
            if (value == null || value.isBlank()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing param: " + key);
            }
            return LocalDate.parse(value);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid date param: " + key);
        }
    }
}
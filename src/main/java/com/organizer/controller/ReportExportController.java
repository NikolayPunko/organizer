package com.organizer.controller;

import com.organizer.service.ReportExportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/reports/export")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN','ORGANIZER')")
public class ReportExportController {

    private final ReportExportService exportService;

    @GetMapping(value = "/{reportKey}.csv", produces = "text/csv")
    public ResponseEntity<byte[]> exportCsv(
            @PathVariable String reportKey,
            @RequestParam Map<String, String> params
    ) {
        byte[] bytes = exportService.exportCsv(reportKey, params);

        String filename = reportKey + ".csv";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(bytes);
    }

    @GetMapping(value = "/{reportKey}.xlsx",
            produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public ResponseEntity<byte[]> exportXlsx(
            @PathVariable String reportKey,
            @RequestParam Map<String, String> params
    ) {
        byte[] bytes = exportService.exportXlsx(reportKey, params);

        String filename = reportKey + ".xlsx";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(bytes);
    }

    @GetMapping(value = "/{reportKey}.pdf", produces = "application/pdf")
    public ResponseEntity<byte[]> exportPdf(
            @PathVariable String reportKey,
            @RequestParam Map<String, String> params
    ) {
        byte[] bytes = exportService.exportPdf(reportKey, params);

        String filename = reportKey + ".pdf";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(bytes);
    }
}
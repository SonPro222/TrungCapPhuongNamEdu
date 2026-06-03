package org.example.trungcapphuongnam.common.export;

import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/export")
public class ExportExcelController {

    private final Map<String, ExcelExportProvider> providerMap;

    public ExportExcelController(List<ExcelExportProvider> providers) {
        this.providerMap = providers.stream()
                .collect(Collectors.toMap(
                        ExcelExportProvider::getType,
                        Function.identity()
                ));
    }

    @GetMapping("/excel/{type}")
    public ResponseEntity<byte[]> exportExcel(
            @PathVariable String type,
            @RequestParam Map<String, String> params
    ) {
        ExcelExportProvider provider = providerMap.get(type);

        if (provider == null) {
            throw new IllegalArgumentException("Không hỗ trợ export Excel cho type: " + type);
        }

        ExcelExportData<?> exportData = provider.getExportData(params);
        byte[] fileBytes = export(exportData);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
        ));

        headers.setContentDisposition(ContentDisposition.attachment()
                .filename(exportData.getFileName(), StandardCharsets.UTF_8)
                .build());

        return ResponseEntity.ok()
                .headers(headers)
                .body(fileBytes);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private byte[] export(ExcelExportData exportData) {
        return ExcelExportUtil.exportToExcel(exportData);
    }
}
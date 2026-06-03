package org.example.trungcapphuongnam.common.export;

import java.util.Map;

public interface ExcelExportProvider {

    String getType();

    ExcelExportData<?> getExportData();

    default ExcelExportData<?> getExportData(Map<String, String> params) {
        return getExportData();
    }
}
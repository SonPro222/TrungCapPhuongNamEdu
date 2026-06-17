package org.example.trungcapphuongnam.common.export;


import java.util.List;

public class ExcelExportData<T> {

    private final String sheetName;
    private final String fileName;
    private final List<ExcelColumn<T>> columns;
    private final List<T> data;

    public ExcelExportData(
            String sheetName,
            String fileName,
            List<ExcelColumn<T>> columns,
            List<T> data
    ) {
        this.sheetName = sheetName;
        this.fileName = fileName;
        this.columns = columns;
        this.data = data;
    }

    public String getSheetName() {
        return sheetName;
    }

    public String getFileName() {
        return fileName;
    }

    public List<ExcelColumn<T>> getColumns() {
        return columns;
    }

    public List<T> getData() {
        return data;
    }
}
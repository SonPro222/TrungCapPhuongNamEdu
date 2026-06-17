package org.example.trungcapphuongnam.common.export;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ExcelExportUtil {

    private ExcelExportUtil() {
    }

    public static <T> byte[] exportToExcel(ExcelExportData<T> exportData) {
        try (
                Workbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream()
        ) {
            Sheet sheet = workbook.createSheet(exportData.getSheetName());

            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle dateStyle = createDateStyle(workbook);

            createHeaderRow(sheet, exportData.getColumns(), headerStyle);
            createDataRows(sheet, exportData.getColumns(), exportData.getData(), dateStyle);
            autoSizeColumns(sheet, exportData.getColumns().size());

            workbook.write(outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Không thể xuất file Excel", e);
        }
    }

    private static <T> void createHeaderRow(
            Sheet sheet,
            List<ExcelColumn<T>> columns,
            CellStyle headerStyle
    ) {
        Row headerRow = sheet.createRow(0);

        for (int i = 0; i < columns.size(); i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns.get(i).getHeader());
            cell.setCellStyle(headerStyle);
        }
    }

    private static <T> void createDataRows(
            Sheet sheet,
            List<ExcelColumn<T>> columns,
            List<T> data,
            CellStyle dateStyle
    ) {
        for (int rowIndex = 0; rowIndex < data.size(); rowIndex++) {
            Row row = sheet.createRow(rowIndex + 1);
            T item = data.get(rowIndex);

            for (int colIndex = 0; colIndex < columns.size(); colIndex++) {
                Object value = columns.get(colIndex).getValueExtractor().apply(item);
                Cell cell = row.createCell(colIndex);
                setCellValue(cell, value, dateStyle);
            }
        }
    }

    private static void setCellValue(Cell cell, Object value, CellStyle dateStyle) {
        if (value == null) {
            cell.setCellValue("");
            return;
        }

        if (value instanceof Number number) {
            cell.setCellValue(number.doubleValue());
            return;
        }

        if (value instanceof Boolean bool) {
            cell.setCellValue(bool);
            return;
        }

        if (value instanceof LocalDate localDate) {
            cell.setCellValue(localDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            cell.setCellStyle(dateStyle);
            return;
        }

        if (value instanceof LocalDateTime localDateTime) {
            cell.setCellValue(localDateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
            cell.setCellStyle(dateStyle);
            return;
        }

        cell.setCellValue(String.valueOf(value));
    }

    private static CellStyle createHeaderStyle(Workbook workbook) {
        Font font = workbook.createFont();
        font.setBold(true);

        CellStyle style = workbook.createCellStyle();
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);

        return style;
    }

    private static CellStyle createDateStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.LEFT);
        return style;
    }

    private static void autoSizeColumns(Sheet sheet, int totalColumns) {
        for (int i = 0; i < totalColumns; i++) {
            sheet.autoSizeColumn(i);
        }
    }
}
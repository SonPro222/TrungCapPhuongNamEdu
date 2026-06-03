package org.example.trungcapphuongnam.module.sinhVien.service.impl;

import org.example.trungcapphuongnam.common.export.ExcelColumn;
import org.example.trungcapphuongnam.common.export.ExcelExportData;
import org.example.trungcapphuongnam.common.export.ExcelExportProvider;
import org.example.trungcapphuongnam.module.sinhVien.dto.response.SinhVienExportExcelResponse;
import org.example.trungcapphuongnam.module.sinhVien.entity.SinhVien;
import org.example.trungcapphuongnam.module.sinhVien.repository.SinhVienRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class SinhVienExcelExportProvider implements ExcelExportProvider {

    private final SinhVienRepository sinhVienRepository;

    public SinhVienExcelExportProvider(SinhVienRepository sinhVienRepository) {
        this.sinhVienRepository = sinhVienRepository;
    }

    @Override
    public String getType() {
        return "sinh-vien-toan-truong";
    }

    @Override
    public ExcelExportData<SinhVienExportExcelResponse> getExportData() {
        List<SinhVienExportExcelResponse> sinhViens = sinhVienRepository.findAll()
                .stream()
                .map(this::toExportResponse)
                .toList();

        AtomicInteger index = new AtomicInteger(1);

        List<ExcelColumn<SinhVienExportExcelResponse>> columns = List.of(
                new ExcelColumn<>("STT", sv -> index.getAndIncrement()),
                new ExcelColumn<>("Mã sinh viên", SinhVienExportExcelResponse::getMaSinhVien),
                new ExcelColumn<>("Họ tên", SinhVienExportExcelResponse::getHoTen),
                new ExcelColumn<>("Ngày sinh", SinhVienExportExcelResponse::getNgaySinh),
                new ExcelColumn<>("Giới tính", SinhVienExportExcelResponse::getGioiTinh),
                new ExcelColumn<>("Email", SinhVienExportExcelResponse::getEmail),
                new ExcelColumn<>("Số điện thoại", SinhVienExportExcelResponse::getSoDienThoai),
                new ExcelColumn<>("Số CCCD", SinhVienExportExcelResponse::getSoCccd),
                new ExcelColumn<>("Ngày cấp CCCD", SinhVienExportExcelResponse::getNgayCapCccd),
                new ExcelColumn<>("Nơi cấp CCCD", SinhVienExportExcelResponse::getNoiCapCccd),
                new ExcelColumn<>("Địa chỉ", SinhVienExportExcelResponse::getDiaChi),
                new ExcelColumn<>("Địa chỉ thường trú", SinhVienExportExcelResponse::getDiaChiThuongTru),
                new ExcelColumn<>("Bằng cấp", SinhVienExportExcelResponse::getBangCap),
                new ExcelColumn<>("Năm tốt nghiệp", SinhVienExportExcelResponse::getNamTotNghiep),
                new ExcelColumn<>("Trường tốt nghiệp", SinhVienExportExcelResponse::getTruongTotNghiep),
                new ExcelColumn<>("Trạng thái", SinhVienExportExcelResponse::getTrangThai),
                new ExcelColumn<>("Ngày tạo hồ sơ", SinhVienExportExcelResponse::getCreatedAt)
        );

        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

        return new ExcelExportData<>(
                "Sinh viên toàn trường",
                "danh-sach-sinh-vien-toan-truong-" + now + ".xlsx",
                columns,
                sinhViens
        );
    }

    private SinhVienExportExcelResponse toExportResponse(SinhVien sinhVien) {
        return new SinhVienExportExcelResponse(
                sinhVien.getMaSinhVien(),
                sinhVien.getHoTen(),
                sinhVien.getNgaySinh(),
                sinhVien.getGioiTinh(),
                sinhVien.getEmail(),
                sinhVien.getSoDienThoai(),
                sinhVien.getSoCccd(),
                sinhVien.getNgayCapCccd(),
                sinhVien.getNoiCapCccd(),
                sinhVien.getDiaChi(),
                sinhVien.getDiaChiThuongTru(),
                sinhVien.getBangCap(),
                sinhVien.getNamTotNghiep(),
                sinhVien.getTruongTotNghiep(),
                sinhVien.getTrangThai(),
                sinhVien.getCreatedAt()
        );
    }
}
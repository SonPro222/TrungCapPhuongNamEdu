package org.example.trungcapphuongnam.module.sinhVien.service.impl;

import org.example.trungcapphuongnam.common.export.ExcelColumn;
import org.example.trungcapphuongnam.common.export.ExcelExportData;
import org.example.trungcapphuongnam.common.export.ExcelExportProvider;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.ChuongTrinh;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.ChuongTrinhVersion;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.ChuongTrinhRepository;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.ChuongTrinhVersionRepository;
import org.example.trungcapphuongnam.module.daoTao.entity.Nganh;
import org.example.trungcapphuongnam.module.daoTao.repository.NganhRepository;
import org.example.trungcapphuongnam.module.sinhVien.dto.response.SinhVienTheoNganhVersionExportResponse;
import org.example.trungcapphuongnam.module.sinhVien.entity.SinhVien;
import org.example.trungcapphuongnam.module.sinhVien.entity.SinhVienChuongTrinh;
import org.example.trungcapphuongnam.module.sinhVien.enums.SinhVienTrangThai;
import org.example.trungcapphuongnam.module.sinhVien.repository.SinhVienChuongTrinhRepository;
import org.example.trungcapphuongnam.module.sinhVien.repository.SinhVienRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class SinhVienTheoNganhVersionExcelExportProvider implements ExcelExportProvider {

    private final SinhVienRepository sinhVienRepository;
    private final SinhVienChuongTrinhRepository sinhVienChuongTrinhRepository;
    private final ChuongTrinhVersionRepository chuongTrinhVersionRepository;
    private final ChuongTrinhRepository chuongTrinhRepository;
    private final NganhRepository nganhRepository;

    public SinhVienTheoNganhVersionExcelExportProvider(
            SinhVienRepository sinhVienRepository,
            SinhVienChuongTrinhRepository sinhVienChuongTrinhRepository,
            ChuongTrinhVersionRepository chuongTrinhVersionRepository,
            ChuongTrinhRepository chuongTrinhRepository,
            NganhRepository nganhRepository
    ) {
        this.sinhVienRepository = sinhVienRepository;
        this.sinhVienChuongTrinhRepository = sinhVienChuongTrinhRepository;
        this.chuongTrinhVersionRepository = chuongTrinhVersionRepository;
        this.chuongTrinhRepository = chuongTrinhRepository;
        this.nganhRepository = nganhRepository;
    }

    @Override
    public String getType() {
        return "sinh-vien-theo-nganh-version";
    }

    @Override
    public ExcelExportData<SinhVienTheoNganhVersionExportResponse> getExportData() {
        return getExportData(Map.of());
    }

    @Override
    public ExcelExportData<SinhVienTheoNganhVersionExportResponse> getExportData(Map<String, String> params) {
        Long nganhId = parseLong(params.get("nganhId"));
        Long chuongTrinhId = parseLong(params.get("chuongTrinhId"));
        Long chuongTrinhVersionId = parseLong(params.get("chuongTrinhVersionId"));
        String maSinhVien = parseString(params.get("maSinhVien"));
        String hoTen = parseString(params.get("hoTen"));
        SinhVienTrangThai trangThai = parseTrangThai(params.get("trangThai"));

        Map<Long, SinhVienChuongTrinh> sinhVienChuongTrinhMap = sinhVienChuongTrinhRepository.findAll()
                .stream()
                .filter(item -> item.getSinhVien() != null && item.getSinhVien().getId() != null)
                .collect(Collectors.toMap(
                        item -> item.getSinhVien().getId(),
                        Function.identity(),
                        (oldItem, newItem) -> {
                            Long oldId = oldItem.getId() == null ? 0L : oldItem.getId();
                            Long newId = newItem.getId() == null ? 0L : newItem.getId();
                            return newId > oldId ? newItem : oldItem;
                        }
                ));

        Map<Long, ChuongTrinhVersion> versionMap = chuongTrinhVersionRepository.findAll()
                .stream()
                .collect(Collectors.toMap(ChuongTrinhVersion::getId, Function.identity()));

        Map<Long, ChuongTrinh> chuongTrinhMap = chuongTrinhRepository.findAll()
                .stream()
                .collect(Collectors.toMap(ChuongTrinh::getId, Function.identity()));

        Map<Long, Nganh> nganhMap = nganhRepository.findAll()
                .stream()
                .collect(Collectors.toMap(Nganh::getId, Function.identity()));

        List<SinhVienTheoNganhVersionExportResponse> data = sinhVienRepository.findAll()
                .stream()
                .map(sv -> toExportResponse(sv, sinhVienChuongTrinhMap, versionMap, chuongTrinhMap, nganhMap))
                .filter(item -> matchText(item.getMaSinhVien(), maSinhVien))
                .filter(item -> matchText(item.getHoTen(), hoTen))
                .filter(item -> trangThai == null || item.getTrangThai() == trangThai)
                .filter(item -> matchNganh(item, nganhId, nganhMap))
                .filter(item -> matchChuongTrinh(item, chuongTrinhId, chuongTrinhMap))
                .filter(item -> matchVersion(item, chuongTrinhVersionId, versionMap))
                .sorted(
                        Comparator
                                .comparing(SinhVienTheoNganhVersionExportResponse::getTenNganh, Comparator.nullsLast(String::compareToIgnoreCase))
                                .thenComparing(SinhVienTheoNganhVersionExportResponse::getChuongTrinh, Comparator.nullsLast(String::compareToIgnoreCase))
                                .thenComparing(SinhVienTheoNganhVersionExportResponse::getVersion, Comparator.nullsLast(String::compareToIgnoreCase))
                                .thenComparing(SinhVienTheoNganhVersionExportResponse::getMaSinhVien, Comparator.nullsLast(String::compareToIgnoreCase))
                )
                .toList();

        return buildExportData(data);
    }

    private SinhVienTheoNganhVersionExportResponse toExportResponse(
            SinhVien sinhVien,
            Map<Long, SinhVienChuongTrinh> sinhVienChuongTrinhMap,
            Map<Long, ChuongTrinhVersion> versionMap,
            Map<Long, ChuongTrinh> chuongTrinhMap,
            Map<Long, Nganh> nganhMap
    ) {
        SinhVienChuongTrinh svct = sinhVienChuongTrinhMap.get(sinhVien.getId());

        ChuongTrinhVersion version = null;
        ChuongTrinh chuongTrinh = null;
        Nganh nganh = null;

        if (svct != null && svct.getChuongTrinhVersionId() != null) {
            version = versionMap.get(svct.getChuongTrinhVersionId());
        }

        if (version != null && version.getChuongTrinhId() != null) {
            chuongTrinh = chuongTrinhMap.get(version.getChuongTrinhId());
        }

        if (chuongTrinh != null && chuongTrinh.getNganhId() != null) {
            nganh = nganhMap.get(chuongTrinh.getNganhId());
        }

        return new SinhVienTheoNganhVersionExportResponse(
                sinhVien.getMaSinhVien(),
                sinhVien.getHoTen(),
                sinhVien.getEmail(),
                nganh != null ? nganh.getTenNganh() : "",
                chuongTrinh != null ? chuongTrinh.getMaChuongTrinh() : "",
                version != null ? version.getMaVersion() : "",
                svct != null ? svct.getNgayNhapHoc() : null,
                sinhVien.getTrangThai()
        );
    }

    private ExcelExportData<SinhVienTheoNganhVersionExportResponse> buildExportData(
            List<SinhVienTheoNganhVersionExportResponse> data
    ) {
        AtomicInteger index = new AtomicInteger(1);

        List<ExcelColumn<SinhVienTheoNganhVersionExportResponse>> columns = List.of(
                new ExcelColumn<>("STT", item -> index.getAndIncrement()),
                new ExcelColumn<>("Mã SV", SinhVienTheoNganhVersionExportResponse::getMaSinhVien),
                new ExcelColumn<>("Họ và tên", SinhVienTheoNganhVersionExportResponse::getHoTen),
                new ExcelColumn<>("Gmail", SinhVienTheoNganhVersionExportResponse::getGmail),
                new ExcelColumn<>("Ngành", SinhVienTheoNganhVersionExportResponse::getTenNganh),
                new ExcelColumn<>("Chương trình", SinhVienTheoNganhVersionExportResponse::getChuongTrinh),
                new ExcelColumn<>("Version", SinhVienTheoNganhVersionExportResponse::getVersion),
                new ExcelColumn<>("Nhập học", SinhVienTheoNganhVersionExportResponse::getNgayNhapHoc),
                new ExcelColumn<>("Trạng thái", item -> trangThaiLabel(item.getTrangThai()))
        );

        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

        return new ExcelExportData<>(
                "Danh sách sinh viên",
                "danh-sach-sinh-vien-" + now + ".xlsx",
                columns,
                data
        );
    }

    private boolean matchNganh(
            SinhVienTheoNganhVersionExportResponse item,
            Long nganhId,
            Map<Long, Nganh> nganhMap
    ) {
        if (nganhId == null) return true;

        Nganh nganh = nganhMap.get(nganhId);
        if (nganh == null) return false;

        return Objects.equals(item.getTenNganh(), nganh.getTenNganh());
    }

    private boolean matchChuongTrinh(
            SinhVienTheoNganhVersionExportResponse item,
            Long chuongTrinhId,
            Map<Long, ChuongTrinh> chuongTrinhMap
    ) {
        if (chuongTrinhId == null) return true;

        ChuongTrinh chuongTrinh = chuongTrinhMap.get(chuongTrinhId);
        if (chuongTrinh == null) return false;

        return Objects.equals(item.getChuongTrinh(), chuongTrinh.getMaChuongTrinh());
    }

    private boolean matchVersion(
            SinhVienTheoNganhVersionExportResponse item,
            Long chuongTrinhVersionId,
            Map<Long, ChuongTrinhVersion> versionMap
    ) {
        if (chuongTrinhVersionId == null) return true;

        ChuongTrinhVersion version = versionMap.get(chuongTrinhVersionId);
        if (version == null) return false;

        return Objects.equals(item.getVersion(), version.getMaVersion());
    }

    private boolean matchText(String source, String keyword) {
        if (keyword == null) return true;
        if (source == null) return false;

        return source.toLowerCase().contains(keyword.toLowerCase());
    }

    private Long parseLong(String value) {
        String text = parseString(value);
        return text == null ? null : Long.valueOf(text);
    }

    private String parseString(String value) {
        if (value == null) return null;

        String text = value.trim();

        if (text.isBlank()) return null;
        if ("null".equalsIgnoreCase(text)) return null;
        if ("undefined".equalsIgnoreCase(text)) return null;

        return text;
    }

    private SinhVienTrangThai parseTrangThai(String value) {
        String text = parseString(value);

        if (text == null) {
            return null;
        }

        if ("thoi_hoc".equalsIgnoreCase(text)) {
            return SinhVienTrangThai.nghi_hoc;
        }

        return SinhVienTrangThai.valueOf(text);
    }

    private String trangThaiLabel(SinhVienTrangThai trangThai) {
        if (trangThai == null) {
            return "";
        }

        return switch (trangThai) {
            case dang_hoc -> "Đang học";
            case bao_luu -> "Bảo lưu";
            case nghi_hoc -> "Nghỉ học";
            case tot_nghiep -> "Tốt nghiệp";
        };
    }
}
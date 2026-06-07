package org.example.trungcapphuongnam.module.giangDay.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.module.giangDay.GiangDayException;
import org.example.trungcapphuongnam.module.giangDay.dto.request.LichHocRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.response.LichHocPreviewItemResponse;
import org.example.trungcapphuongnam.module.giangDay.dto.response.LichHocResponse;
import org.example.trungcapphuongnam.module.giangDay.dto.response.SinhLichHocPreviewResponse;
import org.example.trungcapphuongnam.module.giangDay.entity.DiemDanh;
import org.example.trungcapphuongnam.module.giangDay.entity.LichHoc;
import org.example.trungcapphuongnam.module.giangDay.entity.SinhVienLopHocPhan;
import org.example.trungcapphuongnam.module.giangDay.enums.TrangThaiDiemDanh;
import org.example.trungcapphuongnam.module.giangDay.enums.TrangThaiLichHoc;
import org.example.trungcapphuongnam.module.giangDay.enums.TrangThaiSinhVienLopHocPhan;
import org.example.trungcapphuongnam.module.giangDay.mapper.LichHocMapper;
import org.example.trungcapphuongnam.module.giangDay.repository.DiemDanhRepository;
import org.example.trungcapphuongnam.module.giangDay.repository.LichHocRepository;
import org.example.trungcapphuongnam.module.giangDay.repository.SinhVienLopHocPhanRepository;
import org.example.trungcapphuongnam.module.giangDay.validator.LichHocValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LichHocTuDongTransactionService {

    private final LichHocRepository lichHocRepository;
    private final LichHocMapper lichHocMapper;
    private final LichHocValidator lichHocValidator;
    private final SinhVienLopHocPhanRepository sinhVienLopHocPhanRepository;
    private final DiemDanhRepository diemDanhRepository;

    /**
     * Luu lich tu dong trong transaction rieng cho tung lop hoc phan.
     * Dieu nay tranh loi rollback-only cua batch lon: lop nao loi thi rollback rieng lop do,
     * cac lop khac khong bi anh huong.
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<LichHocResponse> luuLichTuDongTuPreview(Long lopHocPhanId, SinhLichHocPreviewResponse preview) {
        if (lopHocPhanId == null) {
            throw new GiangDayException("Lớp học phần không hợp lệ");
        }
        if (preview == null || !Boolean.TRUE.equals(preview.getHopLe())) {
            throw new GiangDayException("Lịch preview chưa hợp lệ, không thể lưu");
        }
        if (preview.getItems() == null || preview.getItems().isEmpty()) {
            return List.of();
        }

        List<LichHocRequest> requests = new ArrayList<>();
        for (LichHocPreviewItemResponse item : preview.getItems()) {
            if (item == null) {
                continue;
            }
            LichHocRequest request = LichHocRequest.builder()
                    .lopHocPhanId(lopHocPhanId)
                    .giaoVienId(item.getGiaoVienId())
                    .phongHocId(item.getPhongHocId())
                    .caHocId(item.getCaHocId())
                    .caHocIds(item.getCaHocId() == null ? null : List.of(item.getCaHocId()))
                    .thuTrongTuan(tinhThuTrongTuanLichHoc(item.getNgayHoc()))
                    .ngayHoc(item.getNgayHoc())
                    .noiDungBuoiHoc(item.getNoiDungBuoiHoc())
                    .trangThai(TrangThaiLichHoc.du_kien)
                    .ghiChu(null)
                    .build();
            requests.add(request);
        }

        if (requests.isEmpty()) {
            return List.of();
        }

        // Validate tat ca truoc khi save bat ky ban ghi nao de tranh save mot phan roi rollback.
        for (LichHocRequest request : requests) {
            lichHocValidator.validateCreate(request);
        }

        List<LichHoc> lichHocs = requests.stream()
                .map(lichHocMapper::toEntity)
                .toList();

        List<LichHoc> saved = lichHocRepository.saveAllAndFlush(lichHocs);
        taoDiemDanhChoCacBuoiHoc(saved);

        return saved.stream()
                .map(lichHocMapper::toResponse)
                .toList();
    }

    private Integer tinhThuTrongTuanLichHoc(LocalDate ngayHoc) {
        if (ngayHoc == null) {
            return null;
        }
        int dayOfWeek = ngayHoc.getDayOfWeek().getValue();
        return dayOfWeek == 7 ? 8 : dayOfWeek + 1;
    }

    private void taoDiemDanhChoCacBuoiHoc(List<LichHoc> lichHocs) {
        if (lichHocs == null || lichHocs.isEmpty()) {
            return;
        }

        List<DiemDanh> diemDanhs = new ArrayList<>();
        for (LichHoc lichHoc : lichHocs) {
            if (lichHoc == null || lichHoc.getId() == null || lichHoc.getTrangThai() == TrangThaiLichHoc.nghi) {
                continue;
            }

            List<SinhVienLopHocPhan> sinhViens = sinhVienLopHocPhanRepository
                    .findByLopHocPhanIdAndTrangThaiInOrderByIdDesc(
                            lichHoc.getLopHocPhanId(),
                            List.of(
                                    TrangThaiSinhVienLopHocPhan.da_dang_ky,
                                    TrangThaiSinhVienLopHocPhan.dang_hoc,
                                    TrangThaiSinhVienLopHocPhan.hoc_lai
                            )
                    );

            for (SinhVienLopHocPhan sinhVien : sinhViens) {
                if (sinhVien.getSinhVienId() == null) {
                    continue;
                }
                if (diemDanhRepository.existsByLichHocIdAndSinhVienId(lichHoc.getId(), sinhVien.getSinhVienId())) {
                    continue;
                }
                diemDanhs.add(DiemDanh.builder()
                        .lichHocId(lichHoc.getId())
                        .sinhVienId(sinhVien.getSinhVienId())
                        .trangThai(TrangThaiDiemDanh.chua_diem_danh)
                        .thoiGianDiemDanh(null)
                        .ghiChu(null)
                        .build());
            }
        }

        if (!diemDanhs.isEmpty()) {
            diemDanhRepository.saveAllAndFlush(diemDanhs);
        }
    }
}

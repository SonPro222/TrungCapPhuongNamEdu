package org.example.trungcapphuongnam.module.diem.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.exception.BadRequestException;
import org.example.trungcapphuongnam.module.diem.repository.CauHinhDanhGiaRepository;
import org.example.trungcapphuongnam.module.diem.repository.DiemChiTietRepository;
import org.example.trungcapphuongnam.module.diem.repository.KetQuaLopHocPhanRepository;
import org.example.trungcapphuongnam.module.diem.service.TinhDiemLopHocPhanService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service chỗ này để dành cho nghiệp vụ tính điểm tổng.
 * Nên viết query riêng lấy diem_chi_tiet + cau_hinh_danh_gia theo lớp,
 * rồi tính tổng theo tỷ lệ.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class TinhDiemLopHocPhanServiceImpl implements TinhDiemLopHocPhanService {

    private final DiemChiTietRepository diemChiTietRepository;
    private final CauHinhDanhGiaRepository cauHinhDanhGiaRepository;
    private final KetQuaLopHocPhanRepository ketQuaLopHocPhanRepository;

    @Override
    public void tinhLaiKetQuaLopHocPhan(Long sinhVienId, Long lopHocPhanId) {
        if (sinhVienId == null || lopHocPhanId == null) {
            throw new BadRequestException("sinhVienId và lopHocPhanId không được để trống");
        }

        // TODO:
        // 1. Lấy tất cả diem_chi_tiet của sinh viên trong lớp.
        // 2. Join cau_hinh_danh_gia để lấy tỷ lệ.
        // 3. Tính diem_tong_ket = SUM(diem * ty_le / 100).
        // 4. Upsert ket_qua_lop_hoc_phan.
        // 5. Khi chốt lớp, cập nhật ket_qua_mon_hoc.
    }
}

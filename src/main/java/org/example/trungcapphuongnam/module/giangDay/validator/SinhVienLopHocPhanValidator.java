package org.example.trungcapphuongnam.module.giangDay.validator;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.ChuongTrinhMon;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.ChuongTrinhMonRepository;
import org.example.trungcapphuongnam.module.giangDay.GiangDayException;
import org.example.trungcapphuongnam.module.giangDay.entity.LopHocPhan;
import org.example.trungcapphuongnam.module.giangDay.entity.LopHocPhanChuongTrinhMon;
import org.example.trungcapphuongnam.module.giangDay.enums.LoaiLopHocPhan;
import org.example.trungcapphuongnam.module.giangDay.enums.TrangThaiLopHocPhan;
import org.example.trungcapphuongnam.module.giangDay.enums.TrangThaiSinhVienLopHocPhan;
import org.example.trungcapphuongnam.module.giangDay.repository.LopHocPhanChuongTrinhMonRepository;
import org.example.trungcapphuongnam.module.giangDay.repository.LopHocPhanRepository;
import org.example.trungcapphuongnam.module.giangDay.repository.SinhVienLopHocPhanRepository;
import org.example.trungcapphuongnam.module.sinhVien.entity.SinhVien;
import org.example.trungcapphuongnam.module.sinhVien.entity.SinhVienChuongTrinh;
import org.example.trungcapphuongnam.module.sinhVien.enums.SinhVienChuongTrinhTrangThai;
import org.example.trungcapphuongnam.module.sinhVien.enums.SinhVienTrangThai;
import org.example.trungcapphuongnam.module.sinhVien.repository.SinhVienChuongTrinhRepository;
import org.example.trungcapphuongnam.module.sinhVien.repository.SinhVienRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SinhVienLopHocPhanValidator {

    private final SinhVienRepository sinhVienRepository;
    private final SinhVienChuongTrinhRepository sinhVienChuongTrinhRepository;
    private final LopHocPhanRepository lopHocPhanRepository;
    private final ChuongTrinhMonRepository chuongTrinhMonRepository;
    private final SinhVienLopHocPhanRepository sinhVienLopHocPhanRepository;
    private final LopHocPhanChuongTrinhMonRepository lopHocPhanChuongTrinhMonRepository;

    public void validateThemSinhVienVaoLop(Long sinhVienId, Long lopHocPhanId) {
        if (sinhVienId == null) {
            throw new GiangDayException("Sinh viên không được để trống");
        }

        if (lopHocPhanId == null) {
            throw new GiangDayException("Lớp học phần không được để trống");
        }

        SinhVien sinhVien = sinhVienRepository.findById(sinhVienId)
                .orElseThrow(() -> new GiangDayException("Sinh viên không tồn tại"));

        LopHocPhan lopHocPhan = lopHocPhanRepository.findById(lopHocPhanId)
                .orElseThrow(() -> new GiangDayException("Lớp học phần không tồn tại"));

        if (sinhVien.getTrangThai() == SinhVienTrangThai.nghi_hoc) {
            throw new GiangDayException("Sinh viên đã nghỉ học, không được phân vào lớp học phần");
        }

        if (sinhVien.getTrangThai() == SinhVienTrangThai.tot_nghiep) {
            throw new GiangDayException("Sinh viên đã tốt nghiệp, không được phân vào lớp học phần");
        }

        if (lopHocPhan.getTrangThai() == TrangThaiLopHocPhan.huy) {
            throw new GiangDayException("Lớp học phần đã hủy, không được thêm sinh viên");
        }

        if (lopHocPhan.getTrangThai() == TrangThaiLopHocPhan.da_ket_thuc) {
            throw new GiangDayException("Lớp học phần đã kết thúc, không được thêm sinh viên");
        }

        if (!sinhVienThuocLopHocPhan(sinhVienId, lopHocPhan)) {
            throw new GiangDayException("Sinh viên không thuộc ngành/version được phép học lớp học phần này");
        }

        SinhVienChuongTrinh sinhVienChuongTrinh = layDangKyChuongTrinhHopLe(sinhVienId, lopHocPhan);

        if (sinhVienChuongTrinh.getTrangThai() == SinhVienChuongTrinhTrangThai.huy) {
            throw new GiangDayException("Đăng ký chương trình của sinh viên đã bị hủy");
        }

        if (sinhVienChuongTrinh.getTrangThai() == SinhVienChuongTrinhTrangThai.hoan_thanh) {
            throw new GiangDayException("Sinh viên đã hoàn thành chương trình, không được phân lớp học phần");
        }

        if (!trangThaiChuongTrinhDuocPhanLop().contains(sinhVienChuongTrinh.getTrangThai())) {
            throw new GiangDayException("Trạng thái chương trình của sinh viên không được phép phân lớp học phần");
        }

        if (sinhVienLopHocPhanRepository.existsBySinhVienIdAndLopHocPhanId(sinhVienId, lopHocPhanId)) {
            throw new GiangDayException("Sinh viên đã có trong lớp học phần này");
        }

        Integer soLuongToiDa = lopHocPhan.getSoLuongToiDa();

        if (soLuongToiDa != null && soLuongToiDa > 0) {
            long soLuongDangHoc = sinhVienLopHocPhanRepository.countByLopHocPhanIdAndTrangThaiIn(
                    lopHocPhanId,
                    trangThaiDangTinhSiSo()
            );

            if (soLuongDangHoc >= soLuongToiDa) {
                throw new GiangDayException("Lớp học phần đã đủ sĩ số tối đa");
            }
        }
    }

    private SinhVienChuongTrinh layDangKyChuongTrinhHopLe(Long sinhVienId, LopHocPhan lopHocPhan) {
        if (lopHocPhan.getLoaiLopHocPhan() == LoaiLopHocPhan.CHUYEN_NGANH) {
            ChuongTrinhMon chuongTrinhMon = chuongTrinhMonRepository.findById(lopHocPhan.getChuongTrinhMonId())
                    .orElseThrow(() -> new GiangDayException("Chương trình môn của lớp học phần không tồn tại"));

            return sinhVienChuongTrinhRepository
                    .findFirstBySinhVienIdAndChuongTrinhVersionId(
                            sinhVienId,
                            chuongTrinhMon.getChuongTrinhVersionId()
                    )
                    .orElseThrow(() -> new GiangDayException("Sinh viên không thuộc version chương trình của lớp học phần này"));
        }

        List<LopHocPhanChuongTrinhMon> danhSachCtm = lopHocPhanChuongTrinhMonRepository
                .findByLopHocPhanId(lopHocPhan.getId());

        if (danhSachCtm.isEmpty()) {
            throw new GiangDayException("Lớp học chung chưa gắn chương trình môn");
        }

        for (LopHocPhanChuongTrinhMon item : danhSachCtm) {
            ChuongTrinhMon chuongTrinhMon = chuongTrinhMonRepository.findById(item.getChuongTrinhMonId())
                    .orElseThrow(() -> new GiangDayException("Chương trình môn được gắn với lớp học chung không tồn tại"));

            if (!chuongTrinhMon.getMonHocId().equals(lopHocPhan.getMonHocId())) {
                throw new GiangDayException("Chương trình môn gắn với lớp học chung không cùng môn học");
            }

            SinhVienChuongTrinh sinhVienChuongTrinh = sinhVienChuongTrinhRepository
                    .findFirstBySinhVienIdAndChuongTrinhVersionId(
                            sinhVienId,
                            chuongTrinhMon.getChuongTrinhVersionId()
                    )
                    .orElse(null);

            if (sinhVienChuongTrinh != null) {
                return sinhVienChuongTrinh;
            }
        }

        throw new GiangDayException("Sinh viên không thuộc version chương trình được phép học lớp học chung này");
    }

    private boolean sinhVienThuocLopHocPhan(Long sinhVienId, LopHocPhan lopHocPhan) {
        if (lopHocPhan.getLoaiLopHocPhan() == null) {
            throw new GiangDayException("Lớp học phần chưa có loại lớp học phần");
        }

        if (lopHocPhan.getLoaiLopHocPhan() == LoaiLopHocPhan.CHUYEN_NGANH) {
            if (lopHocPhan.getChuongTrinhMonId() == null) {
                throw new GiangDayException("Lớp chuyên ngành chưa gắn chương trình môn");
            }

            ChuongTrinhMon ctm = chuongTrinhMonRepository.findById(lopHocPhan.getChuongTrinhMonId())
                    .orElseThrow(() -> new GiangDayException("Chương trình môn của lớp học phần không tồn tại"));

            return sinhVienChuongTrinhRepository.existsBySinhVienIdAndChuongTrinhVersionId(
                    sinhVienId,
                    ctm.getChuongTrinhVersionId()
            );
        }

        if (lopHocPhan.getLoaiLopHocPhan() == LoaiLopHocPhan.HOC_CHUNG) {
            if (lopHocPhan.getMonHocId() == null) {
                throw new GiangDayException("Lớp học chung chưa chọn môn học");
            }

            List<LopHocPhanChuongTrinhMon> danhSachCtm = lopHocPhanChuongTrinhMonRepository
                    .findByLopHocPhanId(lopHocPhan.getId());

            if (danhSachCtm.isEmpty()) {
                throw new GiangDayException("Lớp học chung chưa gắn chương trình môn");
            }

            for (LopHocPhanChuongTrinhMon item : danhSachCtm) {
                ChuongTrinhMon ctm = chuongTrinhMonRepository.findById(item.getChuongTrinhMonId())
                        .orElseThrow(() -> new GiangDayException("Chương trình môn được gắn với lớp học chung không tồn tại"));

                if (!ctm.getMonHocId().equals(lopHocPhan.getMonHocId())) {
                    throw new GiangDayException("Chương trình môn gắn với lớp học chung không cùng môn học");
                }

                boolean thuocVersion = sinhVienChuongTrinhRepository.existsBySinhVienIdAndChuongTrinhVersionId(
                        sinhVienId,
                        ctm.getChuongTrinhVersionId()
                );

                if (thuocVersion) {
                    return true;
                }
            }

            return false;
        }

        throw new GiangDayException("Loại lớp học phần không hợp lệ");
    }

    public List<TrangThaiSinhVienLopHocPhan> trangThaiDangTinhSiSo() {
        return List.of(
                TrangThaiSinhVienLopHocPhan.da_dang_ky,
                TrangThaiSinhVienLopHocPhan.dang_hoc,
                TrangThaiSinhVienLopHocPhan.hoc_lai
        );
    }

    public List<SinhVienChuongTrinhTrangThai> trangThaiChuongTrinhDuocPhanLop() {
        return List.of(
                SinhVienChuongTrinhTrangThai.da_dang_ky,
                SinhVienChuongTrinhTrangThai.da_dong_tien,
                SinhVienChuongTrinhTrangThai.dang_hoc
        );
    }
}
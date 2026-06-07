package org.example.trungcapphuongnam.module.giangDay.validator;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.module.giangDay.GiangDayException;
import org.example.trungcapphuongnam.module.giangDay.dto.request.PhanCongGiangDayRequest;
import org.example.trungcapphuongnam.module.giangDay.entity.GiaoVien;
import org.example.trungcapphuongnam.module.giangDay.entity.LopHocPhan;
import org.example.trungcapphuongnam.module.giangDay.enums.TrangThaiGiaoVien;
import org.example.trungcapphuongnam.module.giangDay.enums.TrangThaiLopHocPhan;
import org.example.trungcapphuongnam.module.giangDay.enums.TrangThaiSinhVienLopHocPhan;
import org.example.trungcapphuongnam.module.giangDay.repository.GiaoVienRepository;
import org.example.trungcapphuongnam.module.giangDay.repository.LopHocPhanRepository;
import org.example.trungcapphuongnam.module.giangDay.repository.PhanCongGiangDayRepository;
import org.example.trungcapphuongnam.module.giangDay.repository.SinhVienLopHocPhanRepository;
import org.springframework.stereotype.Component;
import org.example.trungcapphuongnam.module.giangDay.enums.VaiTroGiangDay;

import java.util.List;
@Component
@RequiredArgsConstructor
public class PhanCongGiangDayValidator {

    private final PhanCongGiangDayRepository repository;
    private final LopHocPhanRepository lopHocPhanRepository;
    private final GiaoVienRepository giaoVienRepository;
    private final SinhVienLopHocPhanRepository sinhVienLopHocPhanRepository;

    public void validateCreate(PhanCongGiangDayRequest request) {
        validateCommon(request);

        if (repository.existsByLopHocPhanIdAndGiaoVienIdAndVaiTro(
                request.getLopHocPhanId(),
                request.getGiaoVienId(),
                request.getVaiTro()
        )) {
            throw new GiangDayException("Giáo viên đã được phân công vai trò này trong lớp học phần");
        }
//        if (request.getVaiTro() == VaiTroGiangDay.giang_vien_chinh
//                && repository.existsByLopHocPhanIdAndVaiTro(
//                request.getLopHocPhanId(),
//                VaiTroGiangDay.giang_vien_chinh
//        )) {
//            throw new GiangDayException("Lớp học phần đã có giảng viên chính");
//        }
    }

    public void validateUpdate(Long id, PhanCongGiangDayRequest request) {
        if (id == null) {
            throw new GiangDayException("Phân công giảng dạy không hợp lệ");
        }

        if (!repository.existsById(id)) {
            throw new GiangDayException("Không tìm thấy phân công giảng dạy cần cập nhật");
        }

        validateCommon(request);

        if (repository.existsByLopHocPhanIdAndGiaoVienIdAndVaiTroAndIdNot(
                request.getLopHocPhanId(),
                request.getGiaoVienId(),
                request.getVaiTro(),
                id
        )) {
            throw new GiangDayException("Giáo viên đã được phân công vai trò này trong lớp học phần");
        }
        if (request.getVaiTro() == VaiTroGiangDay.giang_vien_chinh
                && repository.existsByLopHocPhanIdAndVaiTroAndIdNot(
                request.getLopHocPhanId(),
                VaiTroGiangDay.giang_vien_chinh,
                id
        )) {
            throw new GiangDayException("Lớp học phần đã có giảng viên chính");
        }

    }

    private void validateCommon(PhanCongGiangDayRequest request) {
        if (request == null) {
            throw new GiangDayException("Dữ liệu phân công giảng dạy không hợp lệ");
        }

        if (request.getLopHocPhanId() == null) {
            throw new GiangDayException("Lớp học phần không được để trống");
        }

        if (request.getGiaoVienId() == null) {
            throw new GiangDayException("Giáo viên không được để trống");
        }

        if (request.getVaiTro() == null) {
            throw new GiangDayException("Vai trò giảng dạy không được để trống");
        }

        LopHocPhan lopHocPhan = lopHocPhanRepository.findById(request.getLopHocPhanId())
                .orElseThrow(() -> new GiangDayException("Lớp học phần không tồn tại"));

        GiaoVien giaoVien = giaoVienRepository.findById(request.getGiaoVienId())
                .orElseThrow(() -> new GiangDayException("Giáo viên không tồn tại"));

        if (lopHocPhan.getTrangThai() == TrangThaiLopHocPhan.huy) {
            throw new GiangDayException("Lớp học phần đã hủy, không được phân công giảng dạy");
        }

        if (lopHocPhan.getTrangThai() == TrangThaiLopHocPhan.da_ket_thuc) {
            throw new GiangDayException("Lớp học phần đã kết thúc, không được phân công giảng dạy");
        }

        long soSinhVien = sinhVienLopHocPhanRepository.countByLopHocPhanIdAndTrangThaiIn(
                lopHocPhan.getId(),
                List.of(
                        TrangThaiSinhVienLopHocPhan.da_dang_ky,
                        TrangThaiSinhVienLopHocPhan.dang_hoc,
                        TrangThaiSinhVienLopHocPhan.hoc_lai
                )
        );
        if (soSinhVien < 1) {
            throw new GiangDayException("Lớp học phần chưa có sinh viên đang học, chưa thể phân công giảng dạy");
        }

        if (giaoVien.getTrangThai() != TrangThaiGiaoVien.dang_day) {
            throw new GiangDayException("Chỉ giáo viên đang dạy mới được phân công");
        }
    }
}
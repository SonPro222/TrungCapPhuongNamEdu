package org.example.trungcapphuongnam.module.heThong.repository;


import org.example.trungcapphuongnam.module.heThong.constant.TrangThaiTepDinhKem;
import org.example.trungcapphuongnam.module.heThong.entity.TepDinhKem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TepDinhKemRepository extends JpaRepository<TepDinhKem, Long> {

    List<TepDinhKem> findByTrangThaiOrderByNgayTaoDesc(TrangThaiTepDinhKem trangThai);

    List<TepDinhKem> findByModuleAndNghiepVuAndDoiTuongIdAndTrangThaiOrderByNgayTaoDesc(
            String module,
            String nghiepVu,
            Long doiTuongId,
            TrangThaiTepDinhKem trangThai
    );

    List<TepDinhKem> findByNguoiGuiLoaiAndNguoiGuiIdAndTrangThaiOrderByNgayTaoDesc(
            org.example.trungcapphuongnam.module.heThong.constant.LoaiNguoiGui nguoiGuiLoai,
            Long nguoiGuiId,
            TrangThaiTepDinhKem trangThai
    );
}
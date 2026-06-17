package org.example.trungcapphuongnam.module.heThong.repository;

import org.example.trungcapphuongnam.module.heThong.entity.TepDinhKem;
import org.example.trungcapphuongnam.module.heThong.enums.LoaiNguoiGui;
import org.example.trungcapphuongnam.module.heThong.enums.TrangThaiTepDinhKem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TepDinhKemRepository extends JpaRepository<TepDinhKem, Long> {

    Page<TepDinhKem> findByTrangThai(
            TrangThaiTepDinhKem trangThai,
            Pageable pageable
    );

    Page<TepDinhKem> findByModuleAndNghiepVuAndDoiTuongIdAndTrangThai(
            String module,
            String nghiepVu,
            Long doiTuongId,
            TrangThaiTepDinhKem trangThai,
            Pageable pageable
    );

    Page<TepDinhKem> findByNguoiGuiLoaiAndNguoiGuiIdAndTrangThai(
            LoaiNguoiGui nguoiGuiLoai,
            Long nguoiGuiId,
            TrangThaiTepDinhKem trangThai,
            Pageable pageable
    );

    Page<TepDinhKem> findByModuleAndTrangThai(
            String module,
            TrangThaiTepDinhKem trangThai,
            Pageable pageable
    );

    Page<TepDinhKem> findByModuleAndNghiepVuAndTrangThai(
            String module,
            String nghiepVu,
            TrangThaiTepDinhKem trangThai,
            Pageable pageable
    );
}
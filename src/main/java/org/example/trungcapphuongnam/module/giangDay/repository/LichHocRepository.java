package org.example.trungcapphuongnam.module.giangDay.repository;

import org.example.trungcapphuongnam.module.giangDay.entity.LichHoc;
import org.example.trungcapphuongnam.module.giangDay.enums.TrangThaiLichHoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface LichHocRepository extends JpaRepository<LichHoc, Long>, JpaSpecificationExecutor<LichHoc> {

    @Query("""
        SELECT COUNT(lh)
        FROM LichHoc lh
        WHERE lh.lopHocPhanId = :lopHocPhanId
          AND lh.trangThai <> :trangThaiBoQua
          AND (:idBoQua IS NULL OR lh.id <> :idBoQua)
    """)
    long countSoBuoiDangTinh(
            @Param("lopHocPhanId") Long lopHocPhanId,
            @Param("trangThaiBoQua") TrangThaiLichHoc trangThaiBoQua,
            @Param("idBoQua") Long idBoQua
    );

    boolean existsByLopHocPhanIdAndNgayHocAndCaHocIdAndTrangThaiNot(
            Long lopHocPhanId,
            LocalDate ngayHoc,
            Long caHocId,
            TrangThaiLichHoc trangThai
    );

    boolean existsByLopHocPhanIdAndNgayHocAndCaHocIdAndTrangThaiNotAndIdNot(
            Long lopHocPhanId,
            LocalDate ngayHoc,
            Long caHocId,
            TrangThaiLichHoc trangThai,
            Long id
    );

    boolean existsByGiaoVienIdAndNgayHocAndCaHocIdAndTrangThaiNot(
            Long giaoVienId,
            LocalDate ngayHoc,
            Long caHocId,
            TrangThaiLichHoc trangThai
    );

    boolean existsByGiaoVienIdAndNgayHocAndCaHocIdAndTrangThaiNotAndIdNot(
            Long giaoVienId,
            LocalDate ngayHoc,
            Long caHocId,
            TrangThaiLichHoc trangThai,
            Long id
    );

    boolean existsByPhongHocIdAndNgayHocAndCaHocIdAndTrangThaiNot(
            Long phongHocId,
            LocalDate ngayHoc,
            Long caHocId,
            TrangThaiLichHoc trangThai
    );

    boolean existsByPhongHocIdAndNgayHocAndCaHocIdAndTrangThaiNotAndIdNot(
            Long phongHocId,
            LocalDate ngayHoc,
            Long caHocId,
            TrangThaiLichHoc trangThai,
            Long id
    );

    @Query(value = """
        SELECT EXISTS (
            SELECT 1
            FROM sinh_vien_lop_hoc_phan sv_lop_moi
            JOIN sinh_vien_lop_hoc_phan sv_lop_khac
              ON sv_lop_khac.sinh_vien_id = sv_lop_moi.sinh_vien_id
             AND sv_lop_khac.lop_hoc_phan_id <> :lopHocPhanId
             AND sv_lop_khac.trang_thai IN ('da_dang_ky', 'dang_hoc', 'hoc_lai')
            JOIN lich_hoc lh_khac
              ON lh_khac.lop_hoc_phan_id = sv_lop_khac.lop_hoc_phan_id
             AND lh_khac.ngay_hoc = :ngayHoc
             AND lh_khac.ca_hoc_id = :caHocId
             AND lh_khac.trang_thai <> 'nghi'
             AND (:idBoQua IS NULL OR lh_khac.id <> :idBoQua)
            WHERE sv_lop_moi.lop_hoc_phan_id = :lopHocPhanId
              AND sv_lop_moi.trang_thai IN ('da_dang_ky', 'dang_hoc', 'hoc_lai')
            LIMIT 1
        )
    """, nativeQuery = true)
    boolean existsTrungLichSinhVien(
            @Param("lopHocPhanId") Long lopHocPhanId,
            @Param("ngayHoc") LocalDate ngayHoc,
            @Param("caHocId") Long caHocId,
            @Param("idBoQua") Long idBoQua
    );
}
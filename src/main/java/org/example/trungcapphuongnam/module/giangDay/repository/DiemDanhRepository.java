package org.example.trungcapphuongnam.module.giangDay.repository;

import org.example.trungcapphuongnam.module.giangDay.entity.DiemDanh;
import org.example.trungcapphuongnam.module.giangDay.enums.TrangThaiDiemDanh;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface DiemDanhRepository extends JpaRepository<DiemDanh, Long>, JpaSpecificationExecutor<DiemDanh> {

    Page<DiemDanh> findByLichHocId(Long lichHocId, Pageable pageable);

    Page<DiemDanh> findBySinhVienId(Long sinhVienId, Pageable pageable);

    Page<DiemDanh> findByTrangThai(TrangThaiDiemDanh trangThai, Pageable pageable);

    Page<DiemDanh> findByLichHocIdAndSinhVienId(Long lichHocId, Long sinhVienId, Pageable pageable);

    Page<DiemDanh> findByLichHocIdAndTrangThai(Long lichHocId, TrangThaiDiemDanh trangThai, Pageable pageable);

    Page<DiemDanh> findBySinhVienIdAndTrangThai(Long sinhVienId, TrangThaiDiemDanh trangThai, Pageable pageable);

    Page<DiemDanh> findByLichHocIdAndSinhVienIdAndTrangThai(
            Long lichHocId,
            Long sinhVienId,
            TrangThaiDiemDanh trangThai,
            Pageable pageable
    );

    boolean existsByLichHocIdAndSinhVienId(Long lichHocId, Long sinhVienId);

    boolean existsByLichHocIdAndSinhVienIdAndIdNot(Long lichHocId, Long sinhVienId, Long id);

    @Modifying
    @Query(value = """
            INSERT INTO diem_danh (
                lich_hoc_id,
                sinh_vien_id,
                trang_thai,
                ghi_chu,
                thoi_gian_diem_danh
            )
            SELECT
                :lichHocId,
                svlh.sinh_vien_id,
                :trangThaiDiemDanh,
                NULL,
                NULL
            FROM sinh_vien_lop_hoc_phan svlh
            WHERE svlh.lop_hoc_phan_id = :lopHocPhanId
              AND svlh.trang_thai IN (:trangThaiSinhVien)
              AND svlh.sinh_vien_id IS NOT NULL
              AND NOT EXISTS (
                  SELECT 1
                  FROM diem_danh dd
                  WHERE dd.lich_hoc_id = :lichHocId
                    AND dd.sinh_vien_id = svlh.sinh_vien_id
              )
            """, nativeQuery = true)
    int insertMissingDiemDanhForLichHoc(
            @Param("lichHocId") Long lichHocId,
            @Param("lopHocPhanId") Long lopHocPhanId,
            @Param("trangThaiSinhVien") Collection<String> trangThaiSinhVien,
            @Param("trangThaiDiemDanh") String trangThaiDiemDanh
    );
}

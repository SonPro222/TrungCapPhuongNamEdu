package org.example.trungcapphuongnam.module.sinhVien.repository;

import org.example.trungcapphuongnam.module.sinhVien.entity.SinhVienChuongTrinh;
import org.example.trungcapphuongnam.module.sinhVien.enums.SinhVienChuongTrinhTrangThai;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface SinhVienChuongTrinhRepository extends JpaRepository<SinhVienChuongTrinh, Long> {

    boolean existsBySinhVienIdAndChuongTrinhVersionId(Long sinhVienId, Long chuongTrinhVersionId);

    List<SinhVienChuongTrinh> findBySinhVienId(Long sinhVienId);

    List<SinhVienChuongTrinh> findByChuongTrinhVersionId(Long chuongTrinhVersionId);

    Optional<SinhVienChuongTrinh> findFirstBySinhVienIdAndChuongTrinhVersionId(
            Long sinhVienId,
            Long chuongTrinhVersionId
    );
    boolean existsBySinhVienIdAndChuongTrinhVersionIdAndIdNot(
            Long sinhVienId,
            Long chuongTrinhVersionId,
            Long id
    );

    /**
     * Lấy danh sách sinh viên trong các lớp hành chính đang học version chương trình,
     * lọc theo trạng thái (da_dang_ky, da_dong_tien, dang_hoc).
     */
    List<SinhVienChuongTrinh> findByLopHanhChinhIdInAndChuongTrinhVersionIdAndTrangThaiIn(
            Collection<Long> lopHanhChinhIds,
            Long chuongTrinhVersionId,
            Collection<SinhVienChuongTrinhTrangThai> trangThais
    );

    /** Đếm số SV đang trong một lớp hành chính cụ thể. */
    long countByLopHanhChinhId(Long lopHanhChinhId);

    /** Lấy danh sách sinhVienId (không lazy-load) của SV thuộc một lớp hành chính. */
    @Query("SELECT svct.sinhVien.id FROM SinhVienChuongTrinh svct WHERE svct.lopHanhChinhId = :lopHanhChinhId")
    List<Long> findSinhVienIdsByLopHanhChinhId(@Param("lopHanhChinhId") Long lopHanhChinhId);
}
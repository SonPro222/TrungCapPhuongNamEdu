package org.example.trungcapphuongnam.module.sinhVien.repository;

import org.example.trungcapphuongnam.module.sinhVien.dto.response.SinhVienTheoNganhVersionExportResponse;
import org.example.trungcapphuongnam.module.sinhVien.entity.SinhVien;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.example.trungcapphuongnam.module.giangDay.enums.TrangThaiSinhVienLopHocPhan;
import org.example.trungcapphuongnam.module.sinhVien.enums.SinhVienChuongTrinhTrangThai;
import org.example.trungcapphuongnam.module.sinhVien.enums.SinhVienTrangThai;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface SinhVienRepository extends JpaRepository<SinhVien, Long> {
    boolean existsByMaSinhVien(String maSinhVien);
    boolean existsByEmail(String email);
    boolean existsBySoCccd(String soCccd);
    boolean existsByTaiKhoanId(Long taiKhoanId);

    boolean existsByMaSinhVienAndIdNot(String maSinhVien, Long id);
    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);
    boolean existsByTaiKhoanIdAndIdNot(Long taiKhoanId, Long id);

    List<SinhVien> findByIdIn(Collection<Long> ids);
    Optional<SinhVien> findByTaiKhoanId(Long taiKhoanId);

    @Query("""
        select distinct sv
        from SinhVien sv
        join SinhVienChuongTrinh svct on svct.sinhVien = sv
        join ChuongTrinhVersion ctv on ctv.id = svct.chuongTrinhVersionId
        join ChuongTrinh ct on ct.id = ctv.chuongTrinhId
        where (:nganhId is null or ct.nganhId = :nganhId)
          and (:chuongTrinhId is null or ct.id = :chuongTrinhId)
          and (:chuongTrinhVersionId is null or ctv.id = :chuongTrinhVersionId)
        """)
    Page<SinhVien> findTheoNganhChuongTrinhVersion(
            @Param("nganhId") Long nganhId,
            @Param("chuongTrinhId") Long chuongTrinhId,
            @Param("chuongTrinhVersionId") Long chuongTrinhVersionId,
            Pageable pageable
    );
    @Query("""
        select distinct sv
        from SinhVien sv
        join SinhVienChuongTrinh svct on svct.sinhVien = sv
        where svct.chuongTrinhVersionId in :versionIds
          and svct.trangThai in :trangThaiChuongTrinh
          and sv.trangThai in :trangThaiSinhVien
          and not exists (
              select svlhp.id
              from SinhVienLopHocPhan svlhp
              where svlhp.sinhVienId = sv.id
                and svlhp.lopHocPhanId = :lopHocPhanId
                and svlhp.trangThai in :trangThaiLopHocPhan
          )
        """)
    Page<SinhVien> findSinhVienDuDieuKienChuaVaoLopHocPhan(
            @Param("versionIds") Collection<Long> versionIds,
            @Param("lopHocPhanId") Long lopHocPhanId,
            @Param("trangThaiChuongTrinh") Collection<SinhVienChuongTrinhTrangThai> trangThaiChuongTrinh,
            @Param("trangThaiSinhVien") Collection<SinhVienTrangThai> trangThaiSinhVien,
            @Param("trangThaiLopHocPhan") Collection<TrangThaiSinhVienLopHocPhan> trangThaiLopHocPhan,
            Pageable pageable
    );


}
package org.example.trungcapphuongnam.module.giangDay.repository;

import org.example.trungcapphuongnam.module.giangDay.entity.PhongHoc;
import org.example.trungcapphuongnam.module.giangDay.enums.TrangThaiPhongHoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhongHocRepository extends JpaRepository<PhongHoc, Long>, JpaSpecificationExecutor<PhongHoc> {

    boolean existsByMaPhongIgnoreCase(String maPhong);

    boolean existsByMaPhongIgnoreCaseAndIdNot(String maPhong, Long id);

    @Query("""
            SELECT p
            FROM PhongHoc p
            WHERE p.trangThai = :trangThai
              AND (:soLuongHienTai IS NULL OR p.sucChua IS NULL OR p.sucChua >= :soLuongHienTai)
            ORDER BY CASE WHEN p.sucChua IS NULL THEN 1 ELSE 0 END, p.sucChua ASC, p.tenPhong ASC
            """)
    List<PhongHoc> findPhongUngVien(
            @Param("trangThai") TrangThaiPhongHoc trangThai,
            @Param("soLuongHienTai") Integer soLuongHienTai
    );
}

package org.example.trungcapphuongnam.module.daoTao.repository;

import org.example.trungcapphuongnam.module.daoTao.entity.KhungKy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KhungKyRepository extends JpaRepository<KhungKy, Long> {
    boolean existsByLoaiChuongTrinhIdAndMaKy(Long loaiChuongTrinhId, String maKy);
    boolean existsByLoaiChuongTrinhIdAndMaKyAndIdNot(Long loaiChuongTrinhId, String maKy, Long id);
    boolean existsByLoaiChuongTrinhIdAndThuTu(Long loaiChuongTrinhId, Integer thuTu);
    boolean existsByLoaiChuongTrinhIdAndThuTuAndIdNot(Long loaiChuongTrinhId, Integer thuTu, Long id);
}

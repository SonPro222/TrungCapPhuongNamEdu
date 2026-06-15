package org.example.trungcapphuongnam.module.giangDay.repository;

import org.example.trungcapphuongnam.module.giangDay.entity.GiangVienDangKyGiangDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.List;
import java.util.Optional;

public interface GiangVienDangKyGiangDayRepository extends JpaRepository<GiangVienDangKyGiangDay, Long>, JpaSpecificationExecutor<GiangVienDangKyGiangDay> {
    Optional<GiangVienDangKyGiangDay> findByGiaoVienIdAndKhungKyId(Long giaoVienId, Long khungKyId);
    boolean existsByGiaoVienIdAndKhungKyId(Long giaoVienId, Long khungKyId);
    boolean existsByGiaoVienIdAndKhungKyIdAndIdNot(Long giaoVienId, Long khungKyId, Long id);
    List<GiangVienDangKyGiangDay> findByKhungKyId(Long khungKyId);
    List<GiangVienDangKyGiangDay> findByGiaoVienId(Long giaoVienId);
}

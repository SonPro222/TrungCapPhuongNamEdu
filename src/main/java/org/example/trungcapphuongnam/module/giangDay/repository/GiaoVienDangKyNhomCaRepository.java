package org.example.trungcapphuongnam.module.giangDay.repository;

import org.example.trungcapphuongnam.module.giangDay.entity.GiaoVienDangKyNhomCa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GiaoVienDangKyNhomCaRepository extends JpaRepository<GiaoVienDangKyNhomCa, Long> {

    List<GiaoVienDangKyNhomCa> findByGiaoVienId(Long giaoVienId);

    List<GiaoVienDangKyNhomCa> findByGiaoVienIdAndKhungKyId(Long giaoVienId, Long khungKyId);

    boolean existsByGiaoVienIdAndKhungKyIdAndThuTrongTuanAndCaBatDauIdAndCaKetThucId(
            Long giaoVienId,
            Long khungKyId,
            Integer thuTrongTuan,
            Long caBatDauId,
            Long caKetThucId
    );
}

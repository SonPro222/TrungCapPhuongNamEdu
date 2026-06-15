package org.example.trungcapphuongnam.module.giangDay.repository;

import org.example.trungcapphuongnam.module.giangDay.entity.PhanCongGiangDay;
import org.example.trungcapphuongnam.module.giangDay.enums.VaiTroGiangDay;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PhanCongGiangDayRepository extends JpaRepository<PhanCongGiangDay, Long>, JpaSpecificationExecutor<PhanCongGiangDay> {

    Optional<PhanCongGiangDay> findFirstByLopHocPhanIdAndVaiTro(Long lopHocPhanId, VaiTroGiangDay vaiTro);

    boolean existsByLopHocPhanIdAndGiaoVienId(
            Long lopHocPhanId,
            Long giaoVienId
    );

    boolean existsByLopHocPhanIdAndGiaoVienIdAndVaiTro(
            Long lopHocPhanId,
            Long giaoVienId,
            VaiTroGiangDay vaiTro
    );

    boolean existsByLopHocPhanIdAndGiaoVienIdAndVaiTroAndIdNot(
            Long lopHocPhanId,
            Long giaoVienId,
            VaiTroGiangDay vaiTro,
            Long id
    );
    boolean existsByLopHocPhanIdAndVaiTro(
            Long lopHocPhanId,
            VaiTroGiangDay vaiTro
    );

    boolean existsByLopHocPhanIdAndVaiTroAndIdNot(
            Long lopHocPhanId,
            VaiTroGiangDay vaiTro,
            Long id
    );

    Optional<PhanCongGiangDay> findByLopHocPhanIdAndGiaoVienId(Long lopHocPhanId, Long giaoVienId);

    boolean existsByLopHocPhanId(Long lopHocPhanId);
}
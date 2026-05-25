package org.example.trungcapphuongnam.module.giangDay.repository;

import org.example.trungcapphuongnam.module.giangDay.entity.PhanCongGiangDay;
import org.example.trungcapphuongnam.module.giangDay.enums.VaiTroGiangDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PhanCongGiangDayRepository extends JpaRepository<PhanCongGiangDay, Long>, JpaSpecificationExecutor<PhanCongGiangDay> {

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
}
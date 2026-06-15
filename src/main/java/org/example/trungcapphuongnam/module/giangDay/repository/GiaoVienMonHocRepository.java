package org.example.trungcapphuongnam.module.giangDay.repository;

import org.example.trungcapphuongnam.module.giangDay.entity.GiaoVienMonHoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.List;
import java.util.Optional;

public interface GiaoVienMonHocRepository extends JpaRepository<GiaoVienMonHoc, Long>, JpaSpecificationExecutor<GiaoVienMonHoc> {
    boolean existsByGiaoVienIdAndMonHocId(Long giaoVienId, Long monHocId);
    boolean existsByGiaoVienIdAndMonHocIdAndIdNot(Long giaoVienId, Long monHocId, Long id);
    boolean existsByGiaoVienIdAndMonHocIdAndTrangThai(Long giaoVienId, Long monHocId, String trangThai);
    List<GiaoVienMonHoc> findByGiaoVienId(Long giaoVienId);
    List<GiaoVienMonHoc> findByMonHocId(Long monHocId);
    Optional<GiaoVienMonHoc> findByGiaoVienIdAndMonHocId(Long giaoVienId, Long monHocId);
}

package org.example.trungcapphuongnam.module.lms.repository;

import org.example.trungcapphuongnam.module.lms.entity.CauHoiVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.example.trungcapphuongnam.module.lms.enums.LoaiCauHoi;
import org.example.trungcapphuongnam.module.lms.enums.MucDoCauHoi;
import org.example.trungcapphuongnam.module.lms.enums.TrangThaiCauHoiVersion;

public interface CauHoiVersionRepository extends JpaRepository<CauHoiVersion, Long> {
    Page<CauHoiVersion> findByCauHoiMauId(Long cauHoiMauId, Pageable pageable);
    Page<CauHoiVersion> findByTrangThai(TrangThaiCauHoiVersion trangThai, Pageable pageable);
}

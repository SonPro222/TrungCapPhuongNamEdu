package org.example.trungcapphuongnam.module.lms.repository;

import org.example.trungcapphuongnam.module.lms.entity.BaiTapOnlineCauHoi;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BaiTapOnlineCauHoiRepository extends JpaRepository<BaiTapOnlineCauHoi, Long> {
    List<BaiTapOnlineCauHoi> findByBaiTapOnlineIdOrderByThuTuAsc(Long baiTapOnlineId);

    boolean existsByBaiTapOnlineIdAndCauHoiVersionId(Long baiTapOnlineId, Long cauHoiVersionId);

    void deleteByBaiTapOnlineIdAndCauHoiVersionId(Long baiTapOnlineId, Long cauHoiVersionId);
}

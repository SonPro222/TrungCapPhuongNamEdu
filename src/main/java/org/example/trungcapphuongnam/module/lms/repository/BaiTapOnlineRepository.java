package org.example.trungcapphuongnam.module.lms.repository;

import org.example.trungcapphuongnam.module.lms.entity.BaiTapOnline;
import org.example.trungcapphuongnam.module.lms.enums.TrangThaiBaiTapOnline;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BaiTapOnlineRepository extends JpaRepository<BaiTapOnline, Long> {
    Page<BaiTapOnline> findByChuongTrinhMonId(Long chuongTrinhMonId, Pageable pageable);

    Page<BaiTapOnline> findByTrangThai(TrangThaiBaiTapOnline trangThai, Pageable pageable);
}

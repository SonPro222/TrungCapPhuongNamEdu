package org.example.trungcapphuongnam.module.lms.repository;

import org.example.trungcapphuongnam.module.lms.entity.NganHangCauHoi;
import org.example.trungcapphuongnam.module.lms.enums.TrangThaiSuDung;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NganHangCauHoiRepository extends JpaRepository<NganHangCauHoi, Long> {
    Page<NganHangCauHoi> findByChuongTrinhMonId(Long chuongTrinhMonId, Pageable pageable);

    Page<NganHangCauHoi> findByChuongTrinhMonIdAndTrangThai(
            Long chuongTrinhMonId,
            TrangThaiSuDung trangThai,
            Pageable pageable
    );
}

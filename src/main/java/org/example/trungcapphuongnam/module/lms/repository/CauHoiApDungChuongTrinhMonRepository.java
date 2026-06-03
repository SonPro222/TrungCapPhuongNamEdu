package org.example.trungcapphuongnam.module.lms.repository;

import org.example.trungcapphuongnam.module.lms.entity.CauHoiApDungChuongTrinhMon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.example.trungcapphuongnam.module.lms.enums.TrangThaiCauHoiApDung;

public interface CauHoiApDungChuongTrinhMonRepository extends JpaRepository<CauHoiApDungChuongTrinhMon, Long> {
    Page<CauHoiApDungChuongTrinhMon> findByChuongTrinhMonId(Long chuongTrinhMonId, Pageable pageable);
    Page<CauHoiApDungChuongTrinhMon> findByCauHoiVersionId(Long cauHoiVersionId, Pageable pageable);
    boolean existsByCauHoiVersionIdAndChuongTrinhMonIdAndTrangThai(Long cauHoiVersionId, Long chuongTrinhMonId, TrangThaiCauHoiApDung trangThai);
}

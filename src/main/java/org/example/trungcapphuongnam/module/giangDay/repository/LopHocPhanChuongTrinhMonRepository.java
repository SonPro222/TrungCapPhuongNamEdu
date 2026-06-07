package org.example.trungcapphuongnam.module.giangDay.repository;

import org.example.trungcapphuongnam.module.giangDay.entity.LopHocPhanChuongTrinhMon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LopHocPhanChuongTrinhMonRepository extends JpaRepository<LopHocPhanChuongTrinhMon, Long> {

    List<LopHocPhanChuongTrinhMon> findByLopHocPhanId(Long lopHocPhanId);

    List<LopHocPhanChuongTrinhMon> findByChuongTrinhMonId(Long chuongTrinhMonId);

    Page<LopHocPhanChuongTrinhMon> findByLopHocPhanId(Long lopHocPhanId, Pageable pageable);

    Page<LopHocPhanChuongTrinhMon> findByChuongTrinhMonId(Long chuongTrinhMonId, Pageable pageable);

    Page<LopHocPhanChuongTrinhMon> findByLopHocPhanIdAndChuongTrinhMonId(
            Long lopHocPhanId,
            Long chuongTrinhMonId,
            Pageable pageable
    );

    boolean existsByLopHocPhanIdAndChuongTrinhMonId(Long lopHocPhanId, Long chuongTrinhMonId);

    boolean existsByLopHocPhanIdAndChuongTrinhMonIdAndIdNot(
            Long lopHocPhanId,
            Long chuongTrinhMonId,
            Long id
    );

    void deleteByLopHocPhanId(Long lopHocPhanId);

    boolean existsByChuongTrinhMonId(Long chuongTrinhMonId);

    boolean existsByChuongTrinhMonIdAndLopHocPhanIdNot(Long chuongTrinhMonId, Long lopHocPhanId);
}
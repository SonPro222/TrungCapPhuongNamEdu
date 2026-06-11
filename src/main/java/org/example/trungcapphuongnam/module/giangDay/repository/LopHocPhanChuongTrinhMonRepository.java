package org.example.trungcapphuongnam.module.giangDay.repository;

import org.example.trungcapphuongnam.module.giangDay.entity.LopHocPhanChuongTrinhMon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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

    @Query(value = """
            select lhpcm.chuong_trinh_mon_id
            from lop_hoc_phan_chuong_trinh_mon lhpcm
            where lhpcm.lop_hoc_phan_id = :lopHocPhanId
              and lhpcm.chuong_trinh_mon_id is not null
            order by lhpcm.id asc
            limit 1
            """, nativeQuery = true)
    Optional<Long> findFirstChuongTrinhMonIdByLopHocPhanId(@Param("lopHocPhanId") Long lopHocPhanId);
}
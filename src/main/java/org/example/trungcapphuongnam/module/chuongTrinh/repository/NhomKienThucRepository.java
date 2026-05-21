package org.example.trungcapphuongnam.module.chuongTrinh.repository;

import org.example.trungcapphuongnam.module.chuongTrinh.entity.NhomKienThuc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface NhomKienThucRepository extends JpaRepository<NhomKienThuc, Long> {
    Page<NhomKienThuc> findByChuongTrinhVersionId(Long chuongTrinhVersionId, Pageable pageable);
    void deleteByChuongTrinhVersionId(Long chuongTrinhVersionId);
    boolean existsByChuongTrinhVersionIdAndMa(Long chuongTrinhVersionId, String ma);

    boolean existsByChuongTrinhVersionIdAndMaAndIdNot(Long chuongTrinhVersionId, String ma, Long id);

    boolean existsByChuongTrinhVersionIdAndThuTu(Long chuongTrinhVersionId, Integer thuTu);

    boolean existsByChuongTrinhVersionIdAndThuTuAndIdNot(Long chuongTrinhVersionId, Integer thuTu, Long id);

}

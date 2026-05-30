package org.example.trungcapphuongnam.module.chuongTrinh.repository;

import org.example.trungcapphuongnam.module.chuongTrinh.entity.NhomTuChon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
@Repository
public interface NhomTuChonRepository extends JpaRepository<NhomTuChon, Long>, JpaSpecificationExecutor<NhomTuChon> {
    Page<NhomTuChon> findByChuongTrinhVersionId(
            Long chuongTrinhVersionId,
            Pageable pageable
    );

    boolean existsByChuongTrinhVersionIdAndTen(Long chuongTrinhVersionId, String ten);

    boolean existsByChuongTrinhVersionIdAndTenAndIdNot(Long chuongTrinhVersionId, String ten, Long id);


    boolean existsByChuongTrinhVersionIdAndNhomTuChonGocId(
            Long chuongTrinhVersionId,
            Long nhomTuChonGocId
    );

    boolean existsByChuongTrinhVersionIdAndNhomTuChonGocIdAndIdNot(
            Long chuongTrinhVersionId,
            Long nhomTuChonGocId,
            Long id
    );

    Page<NhomTuChon> findByNhomTuChonGocId(Long nhomTuChonGocId, Pageable pageable);
}

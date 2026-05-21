package org.example.trungcapphuongnam.module.chuongTrinh.repository;

import org.example.trungcapphuongnam.module.chuongTrinh.entity.MonTuChon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
@Repository
public interface MonTuChonRepository extends JpaRepository<MonTuChon, Long>, JpaSpecificationExecutor<MonTuChon> {
    Page<MonTuChon> findByNhomId(
            Long nhomId,
            Pageable pageable
    );

    Page<MonTuChon> findByChuongTrinhMonId(
            Long chuongTrinhMonId,
            Pageable pageable
    );

    Page<MonTuChon> findByNhomIdAndChuongTrinhMonId(
            Long nhomId,
            Long chuongTrinhMonId,
            Pageable pageable
    );
    void deleteByNhomId(Long nhomId);

    void deleteByChuongTrinhMonId(Long chuongTrinhMonId);
    boolean existsByNhomIdAndChuongTrinhMonId(Long nhomId, Long chuongTrinhMonId);

    boolean existsByNhomIdAndChuongTrinhMonIdAndIdNot(Long nhomId, Long chuongTrinhMonId, Long id);
}

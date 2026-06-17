package org.example.trungcapphuongnam.module.chuongTrinh.repository;

import org.example.trungcapphuongnam.module.chuongTrinh.entity.MonTienQuyet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
@Repository
public interface MonTienQuyetRepository extends JpaRepository<MonTienQuyet, Long>, JpaSpecificationExecutor<MonTienQuyet> {
    Page<MonTienQuyet> findByMonId(
            Long monId,
            Pageable pageable
    );
    void deleteByMonIdOrMonDieuKienId(Long monId, Long monDieuKienId);
    boolean existsByMonIdAndMonDieuKienId(Long monId, Long monDieuKienId);

    boolean existsByMonIdAndMonDieuKienIdAndIdNot(Long monId, Long monDieuKienId, Long id);
}

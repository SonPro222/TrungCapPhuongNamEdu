package org.example.trungcapphuongnam.module.daoTao.repository;

import org.example.trungcapphuongnam.module.daoTao.entity.LopHanhChinh;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LopHanhChinhRepository extends JpaRepository<LopHanhChinh, Long> {

    boolean existsByMaLop(String maLop);

    boolean existsByMaLopAndIdNot(String maLop, Long id);

    Page<LopHanhChinh> findByChuongTrinhVersionId(
            Long chuongTrinhVersionId,
            Pageable pageable
    );

    Page<LopHanhChinh> findByKhoaDaoTaoId(
            Long khoaDaoTaoId,
            Pageable pageable
    );

    Page<LopHanhChinh> findByChuongTrinhVersionIdAndKhoaDaoTaoId(
            Long chuongTrinhVersionId,
            Long khoaDaoTaoId,
            Pageable pageable
    );
    void deleteByChuongTrinhVersionId(Long chuongTrinhVersionId);

    void deleteByKhoaDaoTaoId(Long khoaDaoTaoId);
}
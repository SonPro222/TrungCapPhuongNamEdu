package org.example.trungcapphuongnam.module.daoTao.repository;


import org.example.trungcapphuongnam.module.daoTao.entity.KhungKyMau;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KhungKyGocRepository extends JpaRepository<KhungKyMau, Long> {

    Page<KhungKyMau> findByMaKyContainingIgnoreCaseOrTenKyContainingIgnoreCaseOrMoTaContainingIgnoreCase(
            String maKy,
            String tenKy,
            String moTa,
            Pageable pageable
    );

    boolean existsByMaKy(String maKy);

    boolean existsByMaKyAndIdNot(String maKy, Long id);

    boolean existsByThuTu(Integer thuTu);

    boolean existsByThuTuAndIdNot(Integer thuTu, Long id);
}
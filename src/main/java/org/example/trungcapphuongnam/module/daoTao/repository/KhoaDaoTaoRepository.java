package org.example.trungcapphuongnam.module.daoTao.repository;

import org.example.trungcapphuongnam.module.daoTao.entity.KhoaDaoTao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KhoaDaoTaoRepository extends JpaRepository<KhoaDaoTao, Long> {
    boolean existsByMaKhoa(String maKhoa);
    boolean existsByMaKhoaAndIdNot(String maKhoa, Long id);
}

package org.example.trungcapphuongnam.module.daoTao.repository;

import org.example.trungcapphuongnam.module.daoTao.entity.LopHanhChinh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LopHanhChinhRepository extends JpaRepository<LopHanhChinh, Long> {
    boolean existsByMaLop(String maLop);
    boolean existsByMaLopAndIdNot(String maLop, Long id);
}

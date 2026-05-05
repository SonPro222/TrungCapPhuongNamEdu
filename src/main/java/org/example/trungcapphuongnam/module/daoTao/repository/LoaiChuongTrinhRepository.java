package org.example.trungcapphuongnam.module.daoTao.repository;

import org.example.trungcapphuongnam.module.daoTao.entity.LoaiChuongTrinh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoaiChuongTrinhRepository extends JpaRepository<LoaiChuongTrinh, Long> {
    boolean existsByMaLoai(String maLoai);
    boolean existsByMaLoaiAndIdNot(String maLoai, Long id);
}

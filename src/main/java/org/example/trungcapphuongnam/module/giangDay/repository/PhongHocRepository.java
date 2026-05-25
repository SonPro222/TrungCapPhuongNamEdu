package org.example.trungcapphuongnam.module.giangDay.repository;

import org.example.trungcapphuongnam.module.giangDay.entity.PhongHoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PhongHocRepository extends JpaRepository<PhongHoc, Long>, JpaSpecificationExecutor<PhongHoc> {

    boolean existsByMaPhongIgnoreCase(String maPhong);

    boolean existsByMaPhongIgnoreCaseAndIdNot(String maPhong, Long id);
}
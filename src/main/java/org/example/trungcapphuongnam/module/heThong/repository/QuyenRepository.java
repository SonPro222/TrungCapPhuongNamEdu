package org.example.trungcapphuongnam.module.heThong.repository;

import org.example.trungcapphuongnam.module.heThong.entity.Quyen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface QuyenRepository extends JpaRepository<Quyen, Long>, JpaSpecificationExecutor<Quyen> {
}

package org.example.trungcapphuongnam.module.heThong.repository;

import org.example.trungcapphuongnam.module.heThong.entity.TaiKhoanVaiTro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TaiKhoanVaiTroRepository extends JpaRepository<TaiKhoanVaiTro, Long>, JpaSpecificationExecutor<TaiKhoanVaiTro> {

    boolean existsByTaiKhoan_IdAndVaiTro_Id(Long taiKhoanId, Long vaiTroId);
}
package org.example.trungcapphuongnam.module.lms.repository;

import org.example.trungcapphuongnam.module.lms.entity.MaTranDeChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.example.trungcapphuongnam.module.lms.enums.LoaiCauHoi;
import org.example.trungcapphuongnam.module.lms.enums.MucDoCauHoi;

public interface MaTranDeChiTietRepository extends JpaRepository<MaTranDeChiTiet, Long> {
    List<MaTranDeChiTiet> findByMaTranDeId(Long maTranDeId);
    void deleteByMaTranDeId(Long maTranDeId);
}

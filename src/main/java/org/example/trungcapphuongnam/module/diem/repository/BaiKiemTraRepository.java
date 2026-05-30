package org.example.trungcapphuongnam.module.diem.repository;

import org.example.trungcapphuongnam.module.diem.entity.BaiKiemTra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BaiKiemTraRepository extends JpaRepository<BaiKiemTra, Long> {

    /** Lấy toàn bộ bài kiểm tra của lớp học phần. */
    List<BaiKiemTra> findByLopHocPhanId(Long lopHocPhanId);
}
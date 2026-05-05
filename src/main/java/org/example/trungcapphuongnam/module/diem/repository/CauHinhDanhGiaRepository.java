package org.example.trungcapphuongnam.module.diem.repository;

import org.example.trungcapphuongnam.module.diem.entity.CauHinhDanhGia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CauHinhDanhGiaRepository extends JpaRepository<CauHinhDanhGia, Long> {

    boolean existsByLopHocPhanIdAndTenCotDiem(Long lopHocPhanId, String tenCotDiem);
    boolean existsByLopHocPhanIdAndTenCotDiemAndIdNot(Long lopHocPhanId, String tenCotDiem, Long id);
}

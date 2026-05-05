package org.example.trungcapphuongnam.module.diem.repository;

import org.example.trungcapphuongnam.module.diem.entity.KetQuaMonHoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KetQuaMonHocRepository extends JpaRepository<KetQuaMonHoc, Long> {

    boolean existsBySinhVienIdAndChuongTrinhMonIdAndSoLanHoc(Long sinhVienId, Long chuongTrinhMonId, Integer soLanHoc);
    boolean existsBySinhVienIdAndChuongTrinhMonIdAndSoLanHocAndIdNot(Long sinhVienId, Long chuongTrinhMonId, Integer soLanHoc, Long id);
}

package org.example.trungcapphuongnam.module.giangDay.repository;

import org.example.trungcapphuongnam.module.giangDay.entity.SinhVienLopHocPhan;
import org.example.trungcapphuongnam.module.giangDay.enums.TrangThaiSinhVienLopHocPhan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface SinhVienLopHocPhanRepository extends JpaRepository<SinhVienLopHocPhan, Long> {

    boolean existsBySinhVienIdAndLopHocPhanId(Long sinhVienId, Long lopHocPhanId);

    List<SinhVienLopHocPhan> findByLopHocPhanIdOrderByIdDesc(Long lopHocPhanId);

    List<SinhVienLopHocPhan> findByLopHocPhanIdAndTrangThaiInOrderByIdDesc(
            Long lopHocPhanId,
            Collection<TrangThaiSinhVienLopHocPhan> trangThais
    );

    Page<SinhVienLopHocPhan> findByLopHocPhanId(Long lopHocPhanId, Pageable pageable);

    Page<SinhVienLopHocPhan> findBySinhVienId(Long sinhVienId, Pageable pageable);

    Page<SinhVienLopHocPhan> findByTrangThai(TrangThaiSinhVienLopHocPhan trangThai, Pageable pageable);

    Page<SinhVienLopHocPhan> findByLopHocPhanIdAndSinhVienId(
            Long lopHocPhanId,
            Long sinhVienId,
            Pageable pageable
    );

    Page<SinhVienLopHocPhan> findByLopHocPhanIdAndTrangThai(
            Long lopHocPhanId,
            TrangThaiSinhVienLopHocPhan trangThai,
            Pageable pageable
    );

    Page<SinhVienLopHocPhan> findBySinhVienIdAndTrangThai(
            Long sinhVienId,
            TrangThaiSinhVienLopHocPhan trangThai,
            Pageable pageable
    );

    Page<SinhVienLopHocPhan> findByLopHocPhanIdAndSinhVienIdAndTrangThai(
            Long lopHocPhanId,
            Long sinhVienId,
            TrangThaiSinhVienLopHocPhan trangThai,
            Pageable pageable
    );

    long countByLopHocPhanId(Long lopHocPhanId);

    /**
     * Kiểm tra sinh viên đã có lớp học phần của cùng ChuongTrinhMon chưa.
     * Dùng để tránh phân bổ trùng khi auto tạo lớp.
     */
    @Query("SELECT CASE WHEN COUNT(svlhp) > 0 THEN true ELSE false END " +
           "FROM SinhVienLopHocPhan svlhp, LopHocPhan lhp " +
           "WHERE svlhp.lopHocPhanId = lhp.id " +
           "AND svlhp.sinhVienId = :sinhVienId " +
           "AND lhp.chuongTrinhMonId = :chuongTrinhMonId")
    boolean existsBySinhVienIdAndLopCuaChuongTrinhMon(
            @Param("sinhVienId") Long sinhVienId,
            @Param("chuongTrinhMonId") Long chuongTrinhMonId
    );

    long countByLopHocPhanIdAndTrangThaiIn(
            Long lopHocPhanId,
            Collection<TrangThaiSinhVienLopHocPhan> trangThais
    );
    Page<SinhVienLopHocPhan> findByLopHocPhanIdAndTrangThaiIn(
            Long lopHocPhanId,
            Collection<TrangThaiSinhVienLopHocPhan> trangThais,
            Pageable pageable
    );

    /**
     * Kiểm tra xem có bất kỳ sinh viên nào trong tập sinhVienIds đã có LHP không.
     * Dùng để validate hủy chốt tuyển sinh: nếu SV thuộc LHC đã vào LHP thì không cho hủy.
     */
    boolean existsBySinhVienIdIn(Collection<Long> sinhVienIds);
}
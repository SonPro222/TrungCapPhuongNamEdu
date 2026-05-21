package org.example.trungcapphuongnam.module.chuongTrinh.repository;

import org.example.trungcapphuongnam.module.chuongTrinh.entity.QuyDoiDiem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
@Repository
public interface QuyDoiDiemRepository extends JpaRepository<QuyDoiDiem, Long> {
    Page<QuyDoiDiem> findByChuongTrinhMonId(
            Long chuongTrinhMonId,
            Pageable pageable
    );
    void deleteByChuongTrinhMonId(Long chuongTrinhMonId);

    @Query("""
        select count(q) > 0
        from QuyDoiDiem q
        where q.chuongTrinhMonId = :chuongTrinhMonId
          and (:id is null or q.id <> :id)
          and q.nguongTu <= :nguongDen
          and q.nguongDen >= :nguongTu
        """)
    boolean existsOverlap(
            @Param("chuongTrinhMonId") Long chuongTrinhMonId,
            @Param("nguongTu") BigDecimal nguongTu,
            @Param("nguongDen") BigDecimal nguongDen,
            @Param("id") Long id
    );

}

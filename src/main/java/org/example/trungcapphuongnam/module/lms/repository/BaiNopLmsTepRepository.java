package org.example.trungcapphuongnam.module.lms.repository;

import org.example.trungcapphuongnam.module.lms.entity.BaiNopLmsTep;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BaiNopLmsTepRepository extends JpaRepository<BaiNopLmsTep, Long> {
    List<BaiNopLmsTep> findByBaiNopLmsId(Long baiNopLmsId);
    void deleteByBaiNopLmsId(Long baiNopLmsId);
}

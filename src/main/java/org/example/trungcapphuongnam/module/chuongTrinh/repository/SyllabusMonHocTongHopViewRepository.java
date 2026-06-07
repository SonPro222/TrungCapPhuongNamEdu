package org.example.trungcapphuongnam.module.chuongTrinh.repository;

import org.example.trungcapphuongnam.module.chuongTrinh.entity.view.SyllabusMonHocTongHopView;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SyllabusMonHocTongHopViewRepository
        extends JpaRepository<SyllabusMonHocTongHopView, Long> {

    Optional<SyllabusMonHocTongHopView> findByChuongTrinhMonId(Long chuongTrinhMonId);
}
package org.example.trungcapphuongnam.module.chuongTrinh.repository;

import org.example.trungcapphuongnam.module.chuongTrinh.entity.SyllabusMonHocMauFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SyllabusMonHocMauFileRepository extends JpaRepository<SyllabusMonHocMauFile, Long> {

    List<SyllabusMonHocMauFile> findBySyllabusMonHocMauIdOrderByCreatedAtDesc(Long syllabusMonHocMauId);

    Optional<SyllabusMonHocMauFile> findByChecksum(String checksum);

    void deleteBySyllabusMonHocMauId(Long syllabusMonHocMauId);
}

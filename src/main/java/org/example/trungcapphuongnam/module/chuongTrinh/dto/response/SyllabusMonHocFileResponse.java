package org.example.trungcapphuongnam.module.chuongTrinh.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SyllabusMonHocFileResponse {

    private Long id;
    private Long syllabusMonHocId;
    private String tenFile;
    private String loaiFile;
    private Long kichThuoc;
    private String loaiTaiLieu;
    private Boolean laFileNguon;
    private String trangThaiDoc;
    private String loiDoc;
    private String checksum;
    private String ghiChu;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

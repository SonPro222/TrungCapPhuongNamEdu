package org.example.trungcapphuongnam.module.chuongTrinh.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SyllabusMonHocMauFileResponse {

    private Long id;
    private Long syllabusMonHocMauId;
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

    /** Text bóc từ file (chỉ trả khi upload hoặc khi cần, không trả ở list) */
    private String noiDungText;

    /** Cảnh báo trong quá trình upload / parse (nullable) */
    private List<String> canhBao;
}

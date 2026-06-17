package org.example.trungcapphuongnam.module.heThong.dto.request;


import lombok.*;
import org.example.trungcapphuongnam.module.heThong.enums.LoaiNguoiGui;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TepDinhKemRequest {

    private String module;

    private String nghiepVu;

    private Long doiTuongId;

    private LoaiNguoiGui nguoiGuiLoai;

    private Long nguoiGuiId;

    private String nguoiGuiTen;

    private String moTa;
    private Long nganhId;

    private Long chuongTrinhId;

    private Long chuongTrinhVersionId;

    private Long namHocId;

    private Long hocKyId;

    private Long lopHanhChinhId;

    private Long lopHocPhanId;

    private Long monHocId;

    private Long chuongTrinhMonId;

    private Long giangVienId;

    private Long sinhVienId;

    private Long baiHocId;

    private Long baiTapId;

    private Long labId;

    private Long baiNopId;

    private Integer lanNop;

    private Long syllabusChuongTrinhId;

    private Long syllabusMonHocId;

    private Long taiLieuMauId;

    private String tangNghiepVu;
}
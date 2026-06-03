package org.example.trungcapphuongnam.module.lms.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.*;
import org.example.trungcapphuongnam.module.lms.enums.LoaiCauHoi;
import org.example.trungcapphuongnam.module.lms.enums.MucDoCauHoi;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "ma_tran_de_chi_tiet")
public class MaTranDeChiTiet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ma_tran_de_id", nullable = false)
    private Long maTranDeId;

    @Column(name = "syllabus_chuong_bai_id")
    private Long syllabusChuongBaiId;

    @Enumerated(EnumType.STRING)
    @Column(name = "muc_do")
    private MucDoCauHoi mucDo;

    @Enumerated(EnumType.STRING)
    @Column(name = "loai_cau_hoi")
    private LoaiCauHoi loaiCauHoi;

    @Column(name = "so_cau", nullable = false)
    private Integer soCau;

    @Column(name = "diem_moi_cau")
    private BigDecimal diemMoiCau;
}

package org.example.trungcapphuongnam.module.chuongTrinh.entity;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "dieu_kien_mon_hoc")
public class DieuKienMonHoc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "syllabus_mon_id")
    private Long syllabusMonId;

    @Column(name = "loai")
    private String loai;

    @Column(name = "noi_dung")
    private String noiDung;

    @Column(name = "thu_tu")
    private Integer thuTu;

}

package org.example.trungcapphuongnam.module.giangDay.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.ChuongTrinhMon;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.SyllabusChuongBai;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.ChuongTrinhMonRepository;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.SyllabusChuongBaiRepository;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.SyllabusMonHocRepository;
import org.example.trungcapphuongnam.module.giangDay.GiangDayException;
import org.example.trungcapphuongnam.module.giangDay.dto.request.LichHocRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.request.SinhLichHocRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.request.XepLichHangLoatRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.response.GoiYLichHocResponse;
import org.example.trungcapphuongnam.module.giangDay.dto.response.LichHocPreviewItemResponse;
import org.example.trungcapphuongnam.module.giangDay.dto.response.LichHocResponse;
import org.example.trungcapphuongnam.module.giangDay.dto.response.SinhLichHocPreviewResponse;
import org.example.trungcapphuongnam.module.giangDay.dto.response.XepLichHangLoatItemResponse;
import org.example.trungcapphuongnam.module.giangDay.dto.response.XepLichHangLoatResponse;
import org.example.trungcapphuongnam.module.giangDay.entity.CaHoc;
import org.example.trungcapphuongnam.module.giangDay.entity.GiaoVien;
import org.example.trungcapphuongnam.module.giangDay.entity.LopHocPhan;
import org.example.trungcapphuongnam.module.giangDay.entity.PhongHoc;
import org.example.trungcapphuongnam.module.giangDay.entity.GiaoVienKhaDung;
import org.example.trungcapphuongnam.module.giangDay.enums.LoaiDangKyGiangVien;
import org.example.trungcapphuongnam.module.giangDay.enums.TrangThaiGiaoVien;
import org.example.trungcapphuongnam.module.giangDay.enums.TrangThaiLichHoc;
import org.example.trungcapphuongnam.module.giangDay.enums.TrangThaiLopHocPhan;
import org.example.trungcapphuongnam.module.giangDay.enums.TrangThaiPhongHoc;
import org.example.trungcapphuongnam.module.giangDay.enums.TrangThaiSinhVienLopHocPhan;
import org.example.trungcapphuongnam.module.giangDay.enums.VaiTroGiangDay;
import org.example.trungcapphuongnam.module.giangDay.mapper.DieuPhoiGiangDayMapper;
import org.example.trungcapphuongnam.module.giangDay.repository.CaHocRepository;
import org.example.trungcapphuongnam.module.giangDay.repository.GiaoVienKhaDungRepository;
import org.example.trungcapphuongnam.module.giangDay.repository.GiaoVienRepository;
import org.example.trungcapphuongnam.module.giangDay.repository.LichHocRepository;
import org.example.trungcapphuongnam.module.giangDay.repository.LopHocPhanRepository;
import org.example.trungcapphuongnam.module.giangDay.repository.NgayNghiRepository;
import org.example.trungcapphuongnam.module.giangDay.repository.PhanCongGiangDayRepository;
import org.example.trungcapphuongnam.module.giangDay.repository.PhongHocRepository;
import org.example.trungcapphuongnam.module.giangDay.repository.SinhVienLopHocPhanRepository;
import org.example.trungcapphuongnam.module.giangDay.service.DieuPhoiGiangDayService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class DieuPhoiGiangDayServiceImpl implements DieuPhoiGiangDayService {

    private static final List<TrangThaiSinhVienLopHocPhan> TRANG_THAI_SINH_VIEN_DANG_HOC = List.of(
            TrangThaiSinhVienLopHocPhan.da_dang_ky,
            TrangThaiSinhVienLopHocPhan.dang_hoc,
            TrangThaiSinhVienLopHocPhan.hoc_lai
    );

    private final LopHocPhanRepository lopHocPhanRepository;
    private final GiaoVienRepository giaoVienRepository;
    private final PhongHocRepository phongHocRepository;
    private final CaHocRepository caHocRepository;
    private final LichHocRepository lichHocRepository;
    private final SinhVienLopHocPhanRepository sinhVienLopHocPhanRepository;
    private final PhanCongGiangDayRepository phanCongGiangDayRepository;
    private final NgayNghiRepository ngayNghiRepository;
    private final GiaoVienKhaDungRepository giaoVienKhaDungRepository;
    private final ChuongTrinhMonRepository chuongTrinhMonRepository;
    private final SyllabusMonHocRepository syllabusMonHocRepository;
    private final SyllabusChuongBaiRepository syllabusChuongBaiRepository;
    private final LichHocTuDongTransactionService lichHocTuDongTransactionService;
    private final DieuPhoiGiangDayMapper dieuPhoiMapper;

    @Override
    @Transactional(readOnly = true)
    public SinhLichHocPreviewResponse previewSinhLich(Long lopHocPhanId, SinhLichHocRequest request) {
        LopHocPhan lop = lopHocPhanRepository.findById(lopHocPhanId)
                .orElseThrow(() -> new GiangDayException("Lớp học phần không tồn tại"));

        validateRequest(lop, request);
        List<Long> caHocIds = layCaHocIds(request);

        int soBuoiCanXep = lop.getSoBuoiHoc();
        int soBuoiDaXep = (int) lichHocRepository.countSoBuoiDangTinh(
                lopHocPhanId,
                TrangThaiLichHoc.nghi,
                null
        );
        int soBuoiConLai = Math.max(soBuoiCanXep - soBuoiDaXep, 0);

        List<LichHocPreviewItemResponse> items = new ArrayList<>();
        List<String> canhBaoTong = new ArrayList<>();

        LocalDate ngayKetThucGoc = request.getDenNgay();
        boolean choPhepLanKy = Boolean.TRUE.equals(request.getChoPhepLanKy());
        int soNgayLanKyToiDa = chuanHoaSoNgayLanKyToiDa(request);
        LocalDate ngayKetThucTimKiem = choPhepLanKy
                ? ngayKetThucGoc.plusDays(soNgayLanKyToiDa)
                : ngayKetThucGoc;

        // Lấy version/kỳ của lớp học phần để check ngày nghỉ đúng phạm vi
        Long[] versionKy = layChuongTrinhVersionVaKhungKy(lop);
        Long lopVersionId = versionKy[0];
        Long lopKhungKyId = versionKy[1];

        int soNgayNghiBiBoQua = 0;
        LocalDate ngay = request.getTuNgay();
        while (!ngay.isAfter(ngayKetThucTimKiem) && items.size() < soBuoiConLai) {
            int thu = tinhThuTrongTuan(ngay);

            if (request.getThuTrongTuan().contains(thu)) {
                boolean laNgayNghi = ngayNghiRepository.existsNgayNghiApDungTheoPhamVi(ngay, lopVersionId, lopKhungKyId);
                if (laNgayNghi) {
                    soNgayNghiBiBoQua++;
                    canhBaoTong.add("Bỏ qua ngày nghỉ " + ngay);
                } else {
                    for (Long caHocId : caHocIds) {
                        if (items.size() >= soBuoiConLai) {
                            break;
                        }

                        List<String> canhBao = taoCanhBaoSlot(lopHocPhanId, request, ngay, thu, caHocId, lopVersionId, lopKhungKyId);

                        if (canhBao.isEmpty()) {
                            int stt = soBuoiDaXep + items.size() + 1;
                            boolean vuotKhungKy = ngay.isAfter(ngayKetThucGoc);
                            int soNgayVuotKhungKy = vuotKhungKy
                                    ? (int) ChronoUnit.DAYS.between(ngayKetThucGoc, ngay)
                                    : 0;
                            List<String> canhBaoItem = vuotKhungKy
                                    ? List.of("Buổi học bù vượt ngày kết thúc gốc " + ngayKetThucGoc + " " + soNgayVuotKhungKy + " ngày")
                                    : List.of();

                            items.add(dieuPhoiMapper.toPreviewItem(
                                    stt,
                                    ngay,
                                    caHocId,
                                    request.getPhongHocId(),
                                    request.getGiaoVienId(),
                                    taoNoiDungBuoiHoc(lop, stt, soBuoiCanXep, request.getTuDongGanNoiDungSyllabus()),
                                    canhBaoItem,
                                    vuotKhungKy,
                                    vuotKhungKy ? ngayKetThucGoc : null,
                                    soNgayVuotKhungKy
                            ));
                        } else {
                            canhBaoTong.add("Ngày " + ngay + " - ca " + caHocId + ": " + String.join(", ", canhBao));
                        }
                    }
                }
            }

            ngay = ngay.plusDays(1);
        }

        int soBuoiVuotKhungKy = (int) items.stream()
                .filter(item -> Boolean.TRUE.equals(item.getVuotKhungKy()))
                .count();

        boolean hopLe = items.size() == soBuoiConLai;
        if (!hopLe) {
            if (choPhepLanKy) {
                canhBaoTong.add("Không xếp đủ số buổi còn lại trong khoảng ngày đã chọn và "
                        + soNgayLanKyToiDa + " ngày được phép lan kỳ");
            } else {
                canhBaoTong.add("Không xếp đủ số buổi còn lại trong khoảng ngày đã chọn. Có thể bật cho phép lan kỳ để bù các buổi thiếu do ngày nghỉ hoặc xung đột lịch.");
            }
        }
        if (soBuoiVuotKhungKy > 0) {
            canhBaoTong.add("Có " + soBuoiVuotKhungKy + " buổi học bù vượt ngày kết thúc gốc " + ngayKetThucGoc);
        }

        return dieuPhoiMapper.toPreviewResponse(
                hopLe,
                soBuoiCanXep,
                soBuoiDaXep,
                soBuoiConLai,
                items.size(),
                canhBaoTong,
                items,
                choPhepLanKy,
                soNgayLanKyToiDa,
                soBuoiVuotKhungKy,
                soNgayNghiBiBoQua
        );
    }

    @Override
    public List<LichHocResponse> taoLichTuDong(Long lopHocPhanId, SinhLichHocRequest request) {
        SinhLichHocPreviewResponse preview = previewSinhLich(lopHocPhanId, request);
        return lichHocTuDongTransactionService.luuLichTuDongTuPreview(lopHocPhanId, preview);
    }


    @Override
    @Transactional(readOnly = true)
    public List<GoiYLichHocResponse> goiYLichHoc(Long lopHocPhanId, SinhLichHocRequest request) {
        LopHocPhan lop = lopHocPhanRepository.findById(lopHocPhanId)
                .orElseThrow(() -> new GiangDayException("Lớp học phần không tồn tại"));

        validateRequestCoBan(lop, request);

        List<PhongHoc> danhSachPhong = danhSachPhongUngVien(lop, request);
        List<List<Long>> danhSachNhomCa = danhSachNhomCaUngVien(request);

        List<Integer> danhSachThu = request.getThuTrongTuan() == null || request.getThuTrongTuan().isEmpty()
                ? List.of(2, 3, 4, 5, 6, 7)
                : request.getThuTrongTuan();

        int gioiHan = request.getSoPhuongAn() == null || request.getSoPhuongAn() < 1
                ? 5
                : Math.min(request.getSoPhuongAn(), 20);

        List<GoiYLichHocResponse> ketQua = new ArrayList<>();
        for (PhongHoc phong : danhSachPhong) {
            for (List<Long> nhomCaHocIds : danhSachNhomCa) {
                SinhLichHocRequest thuRequest = saoChepRequest(request);
                thuRequest.setPhongHocId(phong.getId());
                thuRequest.setCaHocId(nhomCaHocIds.isEmpty() ? null : nhomCaHocIds.get(0));
                thuRequest.setCaHocIds(new ArrayList<>(nhomCaHocIds));
                thuRequest.setThuTrongTuan(danhSachThu);

                SinhLichHocPreviewResponse preview;
                try {
                    preview = previewSinhLich(lopHocPhanId, thuRequest);
                } catch (RuntimeException ex) {
                    continue;
                }

                int soCanhBao = preview.getCanhBao() == null ? 0 : preview.getCanhBao().size();
                int diemSucChua = phong.getSucChua() == null || lop.getSoLuongHienTai() == null
                        ? 0
                        : Math.max(0, 200 - Math.abs(phong.getSucChua() - lop.getSoLuongHienTai()));
                Long caHocIdScore = nhomCaHocIds.isEmpty() ? null : nhomCaHocIds.get(0);
                int diemGiaoVien = tinhDiemGiaoVien(request.getGiaoVienId(), caHocIdScore);
                int diemUuTien = preview.getSoBuoiXepDuoc() * 1000
                        + diemSucChua
                        - soCanhBao * 25
                        + diemGiaoVien;

                ketQua.add(dieuPhoiMapper.toGoiYResponse(
                        preview,
                        phong.getId(),
                        nhomCaHocIds.isEmpty() ? null : nhomCaHocIds.get(0),
                        danhSachThu,
                        diemUuTien,
                        soCanhBao
                ));
            }
        }

        ketQua.sort(Comparator
                .comparing((GoiYLichHocResponse item) -> Boolean.TRUE.equals(item.getHopLe())).reversed()
                .thenComparing(GoiYLichHocResponse::getDiemUuTien, Comparator.reverseOrder())
                .thenComparing(GoiYLichHocResponse::getSoCanhBao)
        );

        List<GoiYLichHocResponse> rutGon = ketQua.stream().limit(gioiHan).toList();
        for (int i = 0; i < rutGon.size(); i++) {
            rutGon.get(i).setThuTuPhuongAn(i + 1);
        }
        return rutGon;
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public XepLichHangLoatResponse xepLichHangLoat(XepLichHangLoatRequest request) {
        if (request == null || request.getLopHocPhanIds() == null || request.getLopHocPhanIds().isEmpty()) {
            throw new GiangDayException("Phải chọn ít nhất một lớp học phần để xếp hàng loạt");
        }

        boolean chiPreview = !Boolean.FALSE.equals(request.getChiPreview());
        List<XepLichHangLoatItemResponse> items = new ArrayList<>();

        for (Long lopHocPhanId : request.getLopHocPhanIds()) {
            LopHocPhan lop = lopHocPhanRepository.findById(lopHocPhanId).orElse(null);
            if (lop == null) {
                items.add(dieuPhoiMapper.toXepLichHangLoatNotFound(lopHocPhanId));
                continue;
            }

            try {
                SinhLichHocRequest lopRequest = chuanBiRequestXepHangLoat(lopHocPhanId, lop, request);

                SinhLichHocPreviewResponse preview = previewSinhLich(lopHocPhanId, lopRequest);
                if (!Boolean.TRUE.equals(preview.getHopLe())) {
                    items.add(dieuPhoiMapper.toXepLichHangLoatFailure(
                            lop,
                            preview.getCanhBao() == null || preview.getCanhBao().isEmpty()
                                    ? "Không xếp đủ số buổi còn lại"
                                    : String.join("; ", preview.getCanhBao())
                    ));
                    continue;
                }

                List<LichHocResponse> lichDaTao = List.of();
                if (!chiPreview) {
                    // Luu theo preview da kiem tra, trong transaction rieng cho tung lop.
                    // Co che nay chi bo sung lich con thieu vao o trong hop le, khong xoa/ghi de lich cu.
                    lichDaTao = lichHocTuDongTransactionService.luuLichTuDongTuPreview(lopHocPhanId, preview);
                }

                items.add(dieuPhoiMapper.toXepLichHangLoatSuccess(
                        lop,
                        chiPreview,
                        preview,
                        lichDaTao
                ));
            } catch (RuntimeException ex) {
                items.add(dieuPhoiMapper.toXepLichHangLoatFailure(
                        lop,
                        ex.getMessage() == null ? "Không xếp được lịch cho lớp này" : ex.getMessage()
                ));
            }
        }

        int thanhCong = (int) items.stream().filter(item -> Boolean.TRUE.equals(item.getThanhCong())).count();
        return dieuPhoiMapper.toXepLichHangLoatResponse(
                items.size(),
                thanhCong,
                items.size() - thanhCong,
                chiPreview,
                items
        );
    }

    private SinhLichHocRequest chuanBiRequestXepHangLoat(Long lopHocPhanId, LopHocPhan lop, XepLichHangLoatRequest request) {
        SinhLichHocRequest lopRequest = saoChepRequest(request);

        if (lopRequest.getGiaoVienId() == null) {
            phanCongGiangDayRepository.findFirstByLopHocPhanIdAndVaiTro(lopHocPhanId, VaiTroGiangDay.giang_vien_chinh)
                    .ifPresent(phanCong -> lopRequest.setGiaoVienId(phanCong.getGiaoVienId()));
        }

        if (lopRequest.getGiaoVienId() == null) {
            throw new GiangDayException("Lớp học phần chưa có giáo viên chính để xếp tự động");
        }

        validateRequestCoBan(lop, lopRequest);

        boolean thieuPhong = lopRequest.getPhongHocId() == null;
        boolean thieuCa = !coCaHoc(lopRequest);

        if (thieuPhong || thieuCa || Boolean.TRUE.equals(lopRequest.getTuDongChonPhong()) || Boolean.TRUE.equals(lopRequest.getTuDongChonCa())) {
            List<GoiYLichHocResponse> goiY = goiYLichHoc(lopHocPhanId, lopRequest);
            GoiYLichHocResponse phuongAn = goiY.stream()
                    .filter(item -> Boolean.TRUE.equals(item.getHopLe()))
                    .findFirst()
                    .orElse(null);

            if (phuongAn == null) {
                String lyDo = goiY.isEmpty()
                        ? "Không tìm được phòng/ca phù hợp"
                        : (goiY.get(0).getCanhBao() == null || goiY.get(0).getCanhBao().isEmpty()
                        ? "Không tìm được phương án hợp lệ"
                        : String.join("; ", goiY.get(0).getCanhBao()));
                throw new GiangDayException(lyDo);
            }

            if (thieuPhong || Boolean.TRUE.equals(lopRequest.getTuDongChonPhong())) {
                lopRequest.setPhongHocId(phuongAn.getPhongHocId());
            }
            if (thieuCa || Boolean.TRUE.equals(lopRequest.getTuDongChonCa())) {
                Long caHocId = phuongAn.getCaHocId();
                lopRequest.setCaHocId(caHocId);
                lopRequest.setCaHocIds(caHocId == null ? null : List.of(caHocId));
            }
        }

        return lopRequest;
    }

    /**
     * Tính điểm ưu tiên dựa trên loaiDangKy của giáo viên cho ca học đó.
     * uu_tien: 100 + mucDoUuTien * 10
     * kha_dung: 50 + mucDoUuTien * 10
     * ban: không gọi method này vì slot đã bị loại trước đó
     * không có entry: 0
     */
    private int tinhDiemGiaoVien(Long giaoVienId, Long caHocId) {
        if (giaoVienId == null || caHocId == null) {
            return 0;
        }
        List<GiaoVienKhaDung> entries = giaoVienKhaDungRepository.findBestUuTienEntry(giaoVienId, caHocId);
        if (entries.isEmpty()) {
            return 0;
        }
        GiaoVienKhaDung best = entries.get(0);
        if (best.getLoaiDangKy() == null) {
            return 0;
        }
        int mucDo = best.getMucDoUuTien() != null ? best.getMucDoUuTien() : 1;
        if (best.getLoaiDangKy() == LoaiDangKyGiangVien.uu_tien) {
            return 100 + mucDo * 10;
        }
        if (best.getLoaiDangKy() == LoaiDangKyGiangVien.kha_dung) {
            return 50 + mucDo * 10;
        }
        return 0;
    }

    private int chuanHoaSoNgayLanKyToiDa(SinhLichHocRequest request) {
        if (!Boolean.TRUE.equals(request.getChoPhepLanKy())) {
            return 0;
        }
        Integer soNgay = request.getSoNgayLanKyToiDa();
        if (soNgay == null) {
            return 30;
        }
        if (soNgay < 1) {
            throw new GiangDayException("Số ngày lan kỳ tối đa phải lớn hơn 0 khi bật cho phép lan kỳ");
        }
        if (soNgay > 365) {
            throw new GiangDayException("Số ngày lan kỳ tối đa không được vượt quá 365 ngày");
        }
        return soNgay;
    }

    private boolean coCaHoc(SinhLichHocRequest request) {
        return (request.getCaHocIds() != null && request.getCaHocIds().stream().anyMatch(java.util.Objects::nonNull))
                || request.getCaHocId() != null;
    }

    private List<PhongHoc> danhSachPhongUngVien(LopHocPhan lop, SinhLichHocRequest request) {
        if (request.getPhongHocId() != null) {
            PhongHoc phong = phongHocRepository.findById(request.getPhongHocId())
                    .orElseThrow(() -> new GiangDayException("Phòng học không tồn tại"));
            if (phong.getTrangThai() != TrangThaiPhongHoc.dang_su_dung) {
                throw new GiangDayException("Chỉ phòng học đang sử dụng mới được xếp lịch");
            }
            if (phong.getSucChua() != null
                    && lop.getSoLuongHienTai() != null
                    && phong.getSucChua() < lop.getSoLuongHienTai()) {
                throw new GiangDayException("Phòng học không đủ sức chứa");
            }
            return List.of(phong);
        }

        return phongHocRepository.findPhongUngVien(
                TrangThaiPhongHoc.dang_su_dung,
                lop.getSoLuongHienTai()
        );
    }

    private List<List<Long>> danhSachNhomCaUngVien(SinhLichHocRequest request) {
        if (coCaHoc(request)) {
            return List.of(layCaHocIds(request));
        }

        List<List<Long>> ketQua = new ArrayList<>();
        for (Long caHocId : caHocRepository.findAllIdsOrderByGioBatDauAsc()) {
            ketQua.add(List.of(caHocId));
        }
        return ketQua;
    }

    private SinhLichHocRequest saoChepRequest(SinhLichHocRequest source) {
        SinhLichHocRequest target = new SinhLichHocRequest();
        target.setGiaoVienId(source.getGiaoVienId());
        target.setPhongHocId(source.getPhongHocId());
        target.setCaHocId(source.getCaHocId());
        target.setCaHocIds(source.getCaHocIds() == null ? null : new ArrayList<>(source.getCaHocIds()));
        target.setTuNgay(source.getTuNgay());
        target.setDenNgay(source.getDenNgay());
        target.setThuTrongTuan(source.getThuTrongTuan() == null ? null : new ArrayList<>(source.getThuTrongTuan()));
        target.setTuDongGanNoiDungSyllabus(source.getTuDongGanNoiDungSyllabus());
        target.setTuDongChonPhong(source.getTuDongChonPhong());
        target.setTuDongChonCa(source.getTuDongChonCa());
        target.setSoPhuongAn(source.getSoPhuongAn());
        target.setTuDongBuNgayNghi(source.getTuDongBuNgayNghi());
        target.setChoPhepLanKy(source.getChoPhepLanKy());
        target.setSoNgayLanKyToiDa(source.getSoNgayLanKyToiDa());
        return target;
    }

    private void validateRequestCoBan(LopHocPhan lop, SinhLichHocRequest request) {
        if (request == null) {
            throw new GiangDayException("Dữ liệu sinh lịch không hợp lệ");
        }

        if (lop.getTrangThai() == TrangThaiLopHocPhan.huy) {
            throw new GiangDayException("Lớp học phần đã hủy, không được sinh lịch");
        }

        if (lop.getTrangThai() == TrangThaiLopHocPhan.da_ket_thuc) {
            throw new GiangDayException("Lớp học phần đã kết thúc, không được sinh lịch");
        }

        if (lop.getSoBuoiHoc() == null || lop.getSoBuoiHoc() < 1) {
            throw new GiangDayException("Lớp học phần chưa cấu hình số buổi học");
        }

        long soSinhVien = sinhVienLopHocPhanRepository.countByLopHocPhanIdAndTrangThaiIn(
                lop.getId(),
                TRANG_THAI_SINH_VIEN_DANG_HOC
        );
        if (soSinhVien < 1) {
            throw new GiangDayException("Lớp học phần chưa có sinh viên đang học, không được sinh lịch");
        }

        if (request.getGiaoVienId() == null) {
            throw new GiangDayException("Giáo viên không được để trống");
        }

        GiaoVien giaoVien = giaoVienRepository.findById(request.getGiaoVienId())
                .orElseThrow(() -> new GiangDayException("Giáo viên không tồn tại"));
        if (giaoVien.getTrangThai() != TrangThaiGiaoVien.dang_day) {
            throw new GiangDayException("Chỉ giáo viên đang dạy mới được xếp lịch");
        }
        if (!phanCongGiangDayRepository.existsByLopHocPhanIdAndGiaoVienId(lop.getId(), request.getGiaoVienId())) {
            throw new GiangDayException("Giáo viên chưa được phân công cho lớp học phần này");
        }

        if (request.getTuNgay() == null || request.getDenNgay() == null) {
            throw new GiangDayException("Từ ngày và đến ngày không được để trống");
        }
        if (request.getTuNgay().isAfter(request.getDenNgay())) {
            throw new GiangDayException("Từ ngày không được sau đến ngày");
        }

    }

    private void validateRequest(LopHocPhan lop, SinhLichHocRequest request) {
        if (request == null) {
            throw new GiangDayException("Dữ liệu sinh lịch không hợp lệ");
        }

        if (lop.getTrangThai() == TrangThaiLopHocPhan.huy) {
            throw new GiangDayException("Lớp học phần đã hủy, không được sinh lịch");
        }

        if (lop.getTrangThai() == TrangThaiLopHocPhan.da_ket_thuc) {
            throw new GiangDayException("Lớp học phần đã kết thúc, không được sinh lịch");
        }

        if (lop.getSoBuoiHoc() == null || lop.getSoBuoiHoc() < 1) {
            throw new GiangDayException("Lớp học phần chưa cấu hình số buổi học");
        }

        long soSinhVien = sinhVienLopHocPhanRepository.countByLopHocPhanIdAndTrangThaiIn(
                lop.getId(),
                TRANG_THAI_SINH_VIEN_DANG_HOC
        );
        if (soSinhVien < 1) {
            throw new GiangDayException("Lớp học phần chưa có sinh viên đang học, không được sinh lịch");
        }

        if (request.getGiaoVienId() == null) {
            throw new GiangDayException("Giáo viên không được để trống");
        }

        GiaoVien giaoVien = giaoVienRepository.findById(request.getGiaoVienId())
                .orElseThrow(() -> new GiangDayException("Giáo viên không tồn tại"));
        if (giaoVien.getTrangThai() != TrangThaiGiaoVien.dang_day) {
            throw new GiangDayException("Chỉ giáo viên đang dạy mới được xếp lịch");
        }
        if (!phanCongGiangDayRepository.existsByLopHocPhanIdAndGiaoVienId(lop.getId(), request.getGiaoVienId())) {
            throw new GiangDayException("Giáo viên chưa được phân công cho lớp học phần này");
        }

        if (request.getPhongHocId() == null) {
            throw new GiangDayException("Phòng học không được để trống");
        }

        PhongHoc phongHoc = phongHocRepository.findById(request.getPhongHocId())
                .orElseThrow(() -> new GiangDayException("Phòng học không tồn tại"));
        if (phongHoc.getTrangThai() != TrangThaiPhongHoc.dang_su_dung) {
            throw new GiangDayException("Chỉ phòng học đang sử dụng mới được xếp lịch");
        }
        if (phongHoc.getSucChua() != null
                && lop.getSoLuongHienTai() != null
                && phongHoc.getSucChua() < lop.getSoLuongHienTai()) {
            throw new GiangDayException("Phòng học không đủ sức chứa");
        }

        List<Long> caHocIds = layCaHocIds(request);
        List<CaHoc> caHocs = caHocRepository.findAllById(caHocIds);
        if (caHocs.size() != caHocIds.size()) {
            throw new GiangDayException("Ca học không tồn tại");
        }
        if (request.getTuNgay() == null || request.getDenNgay() == null) {
            throw new GiangDayException("Từ ngày và đến ngày không được để trống");
        }
        if (request.getTuNgay().isAfter(request.getDenNgay())) {
            throw new GiangDayException("Từ ngày không được sau đến ngày");
        }


        if (request.getThuTrongTuan() == null || request.getThuTrongTuan().isEmpty()) {
            throw new GiangDayException("Phải chọn ít nhất một thứ trong tuần");
        }
        for (Integer thu : request.getThuTrongTuan()) {
            if (thu == null || thu < 2 || thu > 8) {
                throw new GiangDayException("Thứ trong tuần không hợp lệ");
            }
        }
    }

    private List<String> taoCanhBaoSlot(Long lopHocPhanId, SinhLichHocRequest request, LocalDate ngay, int thu, Long caHocId,
                                         Long chuongTrinhVersionId, Long khungKyId) {
        List<String> canhBao = new ArrayList<>();

        if (ngayNghiRepository.existsNgayNghiApDungTheoPhamVi(ngay, chuongTrinhVersionId, khungKyId)) {
            canhBao.add("Ngày nghỉ");
        }

        if (lichHocRepository.existsByLopHocPhanIdAndNgayHocAndCaHocIdAndTrangThaiNot(
                lopHocPhanId,
                ngay,
                caHocId,
                TrangThaiLichHoc.nghi
        )) {
            canhBao.add("Lớp đã có lịch");
        }

        if (lichHocRepository.existsByGiaoVienIdAndNgayHocAndCaHocIdAndTrangThaiNot(
                request.getGiaoVienId(),
                ngay,
                caHocId,
                TrangThaiLichHoc.nghi
        )) {
            canhBao.add("Giáo viên bận");
        }

        if (giaoVienKhaDungRepository.existsGiaoVienKhongKhaDung(
                request.getGiaoVienId(),
                thu,
                caHocId,
                ngay
        )) {
            canhBao.add("Giáo viên không khả dụng");
        }

        if (lichHocRepository.existsByPhongHocIdAndNgayHocAndCaHocIdAndTrangThaiNot(
                request.getPhongHocId(),
                ngay,
                caHocId,
                TrangThaiLichHoc.nghi
        )) {
            canhBao.add("Phòng bận");
        }

        if (lichHocRepository.existsTrungLichSinhVien(
                lopHocPhanId,
                ngay,
                caHocId,
                null
        )) {
            canhBao.add("Sinh viên trùng lịch");
        }

        return canhBao;
    }

    /**
     * Lấy [chuongTrinhVersionId, khungKyId] từ LopHocPhan.
     * Tra cứu qua chuongTrinhMonId → ChuongTrinhMon.
     * Nếu không xác định được → [null, null] (chỉ check nghỉ toàn trường).
     */
    private Long[] layChuongTrinhVersionVaKhungKy(LopHocPhan lop) {
        if (lop.getChuongTrinhMonId() != null) {
            Optional<ChuongTrinhMon> ctm = chuongTrinhMonRepository.findById(lop.getChuongTrinhMonId());
            if (ctm.isPresent()) {
                return new Long[]{ctm.get().getChuongTrinhVersionId(), ctm.get().getKhungKyId()};
            }
        }
        return new Long[]{null, null};
    }

    private List<Long> layCaHocIds(SinhLichHocRequest request) {
        LinkedHashSet<Long> ids = new LinkedHashSet<>();
        if (request.getCaHocIds() != null) {
            request.getCaHocIds().stream()
                    .filter(java.util.Objects::nonNull)
                    .forEach(ids::add);
        }
        if (ids.isEmpty() && request.getCaHocId() != null) {
            ids.add(request.getCaHocId());
        }
        if (ids.isEmpty()) {
            throw new GiangDayException("Ca học không được để trống");
        }
        return new ArrayList<>(ids);
    }

    private Integer tinhThuTrongTuan(LocalDate ngayHoc) {
        if (ngayHoc == null) return null;
        int dayOfWeek = ngayHoc.getDayOfWeek().getValue();
        return dayOfWeek == 7 ? 8 : dayOfWeek + 1;
    }

    private String taoNoiDungBuoiHoc(LopHocPhan lop, int stt, int tongBuoi, Boolean tuDongGanNoiDungSyllabus) {
        if (!Boolean.TRUE.equals(tuDongGanNoiDungSyllabus) || lop.getChuongTrinhMonId() == null) {
            return "Buổi " + stt;
        }

        return syllabusMonHocRepository.findFirstIdByChuongTrinhMonId(lop.getChuongTrinhMonId())
                .map(syllabusMonHocId -> {
                    List<SyllabusChuongBai> chuongBai = syllabusChuongBaiRepository
                            .findBySyllabusMonIdOrderByThuTuAsc(syllabusMonHocId);
                    if (chuongBai.isEmpty()) {
                        return "Buổi " + stt;
                    }

                    int index = Math.min(
                            chuongBai.size() - 1,
                            (int) Math.floor(((double) (stt - 1) / Math.max(tongBuoi, 1)) * chuongBai.size())
                    );
                    SyllabusChuongBai item = chuongBai.get(index);
                    return "Buổi " + stt + ": " + item.getTen();
                })
                .orElse("Buổi " + stt);
    }
}

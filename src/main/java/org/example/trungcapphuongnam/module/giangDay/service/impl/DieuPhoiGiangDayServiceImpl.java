package org.example.trungcapphuongnam.module.giangDay.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.SyllabusChuongBai;
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
import org.example.trungcapphuongnam.module.giangDay.enums.TrangThaiGiaoVien;
import org.example.trungcapphuongnam.module.giangDay.enums.TrangThaiLichHoc;
import org.example.trungcapphuongnam.module.giangDay.enums.TrangThaiLopHocPhan;
import org.example.trungcapphuongnam.module.giangDay.enums.TrangThaiPhongHoc;
import org.example.trungcapphuongnam.module.giangDay.enums.TrangThaiSinhVienLopHocPhan;
import org.example.trungcapphuongnam.module.giangDay.enums.VaiTroGiangDay;
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
import org.example.trungcapphuongnam.module.giangDay.service.LichHocService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
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
    private final SyllabusMonHocRepository syllabusMonHocRepository;
    private final SyllabusChuongBaiRepository syllabusChuongBaiRepository;
    private final LichHocService lichHocService;

    @Override
    @Transactional(readOnly = true)
    public SinhLichHocPreviewResponse previewSinhLich(Long lopHocPhanId, SinhLichHocRequest request) {
        LopHocPhan lop = lopHocPhanRepository.findById(lopHocPhanId)
                .orElseThrow(() -> new GiangDayException("Lớp học phần không tồn tại"));

        validateRequest(lop, request);

        int soBuoiCanXep = lop.getSoBuoiHoc();
        int soBuoiDaXep = (int) lichHocRepository.countSoBuoiDangTinh(
                lopHocPhanId,
                TrangThaiLichHoc.nghi,
                null
        );
        int soBuoiConLai = Math.max(soBuoiCanXep - soBuoiDaXep, 0);

        List<LichHocPreviewItemResponse> items = new ArrayList<>();
        List<String> canhBaoTong = new ArrayList<>();

        LocalDate ngay = request.getTuNgay();
        while (!ngay.isAfter(request.getDenNgay()) && items.size() < soBuoiConLai) {
            int thu = ngay.getDayOfWeek().getValue();

            if (request.getThuTrongTuan().contains(thu)) {
                List<String> canhBao = taoCanhBaoSlot(lopHocPhanId, request, ngay, thu);

                if (canhBao.isEmpty()) {
                    int stt = soBuoiDaXep + items.size() + 1;
                    items.add(LichHocPreviewItemResponse.builder()
                            .stt(stt)
                            .ngayHoc(ngay)
                            .caHocId(request.getCaHocId())
                            .phongHocId(request.getPhongHocId())
                            .giaoVienId(request.getGiaoVienId())
                            .noiDungBuoiHoc(taoNoiDungBuoiHoc(lop, stt, soBuoiCanXep, request.getTuDongGanNoiDungSyllabus()))
                            .canhBao(List.of())
                            .build());
                } else {
                    canhBaoTong.add("Ngày " + ngay + ": " + String.join(", ", canhBao));
                }
            }

            ngay = ngay.plusDays(1);
        }

        boolean hopLe = items.size() == soBuoiConLai;
        if (!hopLe) {
            canhBaoTong.add("Không xếp đủ số buổi còn lại trong khoảng ngày đã chọn");
        }

        return SinhLichHocPreviewResponse.builder()
                .hopLe(hopLe)
                .soBuoiCanXep(soBuoiCanXep)
                .soBuoiDaXep(soBuoiDaXep)
                .soBuoiConLai(soBuoiConLai)
                .soBuoiXepDuoc(items.size())
                .canhBao(canhBaoTong)
                .items(items)
                .build();
    }

    @Override
    public List<LichHocResponse> taoLichTuDong(Long lopHocPhanId, SinhLichHocRequest request) {
        SinhLichHocPreviewResponse preview = previewSinhLich(lopHocPhanId, request);

        if (!Boolean.TRUE.equals(preview.getHopLe())) {
            throw new GiangDayException("Lịch preview chưa hợp lệ, không thể lưu");
        }

        List<LichHocResponse> result = new ArrayList<>();
        for (LichHocPreviewItemResponse item : preview.getItems()) {
            LichHocRequest lichRequest = LichHocRequest.builder()
                    .lopHocPhanId(lopHocPhanId)
                    .giaoVienId(item.getGiaoVienId())
                    .phongHocId(item.getPhongHocId())
                    .caHocId(item.getCaHocId())
                    .ngayHoc(item.getNgayHoc())
                    .noiDungBuoiHoc(item.getNoiDungBuoiHoc())
                    .trangThai(TrangThaiLichHoc.du_kien)
                    .build();

            result.add(lichHocService.create(lichRequest));
        }

        return result;
    }


    @Override
    @Transactional(readOnly = true)
    public List<GoiYLichHocResponse> goiYLichHoc(Long lopHocPhanId, SinhLichHocRequest request) {
        LopHocPhan lop = lopHocPhanRepository.findById(lopHocPhanId)
                .orElseThrow(() -> new GiangDayException("Lớp học phần không tồn tại"));

        validateRequestCoBan(lop, request);

        List<PhongHoc> danhSachPhong = phongHocRepository.findAll().stream()
                .filter(phong -> phong.getTrangThai() == TrangThaiPhongHoc.dang_su_dung)
                .filter(phong -> phong.getSucChua() == null
                        || lop.getSoLuongHienTai() == null
                        || phong.getSucChua() >= lop.getSoLuongHienTai())
                .toList();

        List<CaHoc> danhSachCa = caHocRepository.findAll().stream()
                .sorted(Comparator.comparing(CaHoc::getGioBatDau))
                .toList();

        List<Integer> danhSachThu = request.getThuTrongTuan() == null || request.getThuTrongTuan().isEmpty()
                ? List.of(1, 2, 3, 4, 5, 6)
                : request.getThuTrongTuan();

        int gioiHan = request.getSoPhuongAn() == null || request.getSoPhuongAn() < 1
                ? 5
                : Math.min(request.getSoPhuongAn(), 20);

        List<GoiYLichHocResponse> ketQua = new ArrayList<>();
        for (PhongHoc phong : danhSachPhong) {
            for (CaHoc ca : danhSachCa) {
                SinhLichHocRequest thuRequest = saoChepRequest(request);
                thuRequest.setPhongHocId(phong.getId());
                thuRequest.setCaHocId(ca.getId());
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
                int diemUuTien = preview.getSoBuoiXepDuoc() * 1000
                        + diemSucChua
                        - soCanhBao * 25;

                ketQua.add(GoiYLichHocResponse.builder()
                        .hopLe(preview.getHopLe())
                        .diemUuTien(diemUuTien)
                        .phongHocId(phong.getId())
                        .caHocId(ca.getId())
                        .thuTrongTuan(danhSachThu)
                        .soBuoiCanXep(preview.getSoBuoiConLai())
                        .soBuoiXepDuoc(preview.getSoBuoiXepDuoc())
                        .soCanhBao(soCanhBao)
                        .canhBao(preview.getCanhBao())
                        .items(preview.getItems())
                        .build());
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
    public XepLichHangLoatResponse xepLichHangLoat(XepLichHangLoatRequest request) {
        if (request == null || request.getLopHocPhanIds() == null || request.getLopHocPhanIds().isEmpty()) {
            throw new GiangDayException("Phải chọn ít nhất một lớp học phần để xếp hàng loạt");
        }

        boolean chiPreview = !Boolean.FALSE.equals(request.getChiPreview());
        List<XepLichHangLoatItemResponse> items = new ArrayList<>();

        for (Long lopHocPhanId : request.getLopHocPhanIds()) {
            LopHocPhan lop = lopHocPhanRepository.findById(lopHocPhanId).orElse(null);
            if (lop == null) {
                items.add(XepLichHangLoatItemResponse.builder()
                        .lopHocPhanId(lopHocPhanId)
                        .thanhCong(false)
                        .thongBao("Lớp học phần không tồn tại")
                        .build());
                continue;
            }

            try {
                SinhLichHocRequest lopRequest = saoChepRequest(request);
                if (lopRequest.getGiaoVienId() == null) {
                    phanCongGiangDayRepository.findFirstByLopHocPhanIdAndVaiTro(lopHocPhanId, VaiTroGiangDay.giang_vien_chinh)
                            .ifPresent(phanCong -> lopRequest.setGiaoVienId(phanCong.getGiaoVienId()));
                }
                List<GoiYLichHocResponse> goiY = goiYLichHoc(lopHocPhanId, lopRequest);
                GoiYLichHocResponse totNhat = goiY.isEmpty() ? null : goiY.get(0);
                if (totNhat == null || !Boolean.TRUE.equals(totNhat.getHopLe())) {
                    items.add(XepLichHangLoatItemResponse.builder()
                            .lopHocPhanId(lop.getId())
                            .maLop(lop.getMaLop())
                            .tenLop(lop.getTenLop())
                            .thanhCong(false)
                            .thongBao("Không tìm được phương án xếp đủ số buổi")
                            .phuongAnTotNhat(totNhat)
                            .build());
                    continue;
                }

                List<LichHocResponse> lichDaTao = List.of();
                if (!chiPreview) {
                    SinhLichHocRequest luuRequest = saoChepRequest(lopRequest);
                    luuRequest.setPhongHocId(totNhat.getPhongHocId());
                    luuRequest.setCaHocId(totNhat.getCaHocId());
                    luuRequest.setThuTrongTuan(totNhat.getThuTrongTuan());
                    lichDaTao = taoLichTuDong(lopHocPhanId, luuRequest);
                }

                items.add(XepLichHangLoatItemResponse.builder()
                        .lopHocPhanId(lop.getId())
                        .maLop(lop.getMaLop())
                        .tenLop(lop.getTenLop())
                        .thanhCong(true)
                        .thongBao(chiPreview ? "Tìm được phương án hợp lệ" : "Đã tạo lịch")
                        .phuongAnTotNhat(totNhat)
                        .lichDaTao(lichDaTao)
                        .build());
            } catch (RuntimeException ex) {
                items.add(XepLichHangLoatItemResponse.builder()
                        .lopHocPhanId(lop.getId())
                        .maLop(lop.getMaLop())
                        .tenLop(lop.getTenLop())
                        .thanhCong(false)
                        .thongBao(ex.getMessage())
                        .build());
            }
        }

        int thanhCong = (int) items.stream().filter(item -> Boolean.TRUE.equals(item.getThanhCong())).count();
        return XepLichHangLoatResponse.builder()
                .tongLop(items.size())
                .soLopThanhCong(thanhCong)
                .soLopLoi(items.size() - thanhCong)
                .chiPreview(chiPreview)
                .items(items)
                .build();
    }

    private SinhLichHocRequest saoChepRequest(SinhLichHocRequest source) {
        SinhLichHocRequest target = new SinhLichHocRequest();
        target.setGiaoVienId(source.getGiaoVienId());
        target.setPhongHocId(source.getPhongHocId());
        target.setCaHocId(source.getCaHocId());
        target.setTuNgay(source.getTuNgay());
        target.setDenNgay(source.getDenNgay());
        target.setThuTrongTuan(source.getThuTrongTuan() == null ? null : new ArrayList<>(source.getThuTrongTuan()));
        target.setTuDongGanNoiDungSyllabus(source.getTuDongGanNoiDungSyllabus());
        target.setTuDongChonPhong(source.getTuDongChonPhong());
        target.setTuDongChonCa(source.getTuDongChonCa());
        target.setSoPhuongAn(source.getSoPhuongAn());
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
        if (lop.getNgayBatDau() != null && request.getTuNgay().isBefore(lop.getNgayBatDau())) {
            throw new GiangDayException("Từ ngày không được trước ngày bắt đầu của lớp học phần");
        }
        if (lop.getNgayKetThuc() != null && request.getDenNgay().isAfter(lop.getNgayKetThuc())) {
            throw new GiangDayException("Đến ngày không được sau ngày kết thúc của lớp học phần");
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

        if (request.getCaHocId() == null) {
            throw new GiangDayException("Ca học không được để trống");
        }
        if (!caHocRepository.existsById(request.getCaHocId())) {
            throw new GiangDayException("Ca học không tồn tại");
        }

        if (request.getTuNgay() == null || request.getDenNgay() == null) {
            throw new GiangDayException("Từ ngày và đến ngày không được để trống");
        }
        if (request.getTuNgay().isAfter(request.getDenNgay())) {
            throw new GiangDayException("Từ ngày không được sau đến ngày");
        }
        if (lop.getNgayBatDau() != null && request.getTuNgay().isBefore(lop.getNgayBatDau())) {
            throw new GiangDayException("Từ ngày không được trước ngày bắt đầu của lớp học phần");
        }
        if (lop.getNgayKetThuc() != null && request.getDenNgay().isAfter(lop.getNgayKetThuc())) {
            throw new GiangDayException("Đến ngày không được sau ngày kết thúc của lớp học phần");
        }

        if (request.getThuTrongTuan() == null || request.getThuTrongTuan().isEmpty()) {
            throw new GiangDayException("Phải chọn ít nhất một thứ trong tuần");
        }
        for (Integer thu : request.getThuTrongTuan()) {
            if (thu == null || thu < 1 || thu > 7) {
                throw new GiangDayException("Thứ trong tuần không hợp lệ");
            }
        }
    }

    private List<String> taoCanhBaoSlot(Long lopHocPhanId, SinhLichHocRequest request, LocalDate ngay, int thu) {
        List<String> canhBao = new ArrayList<>();

        if (ngayNghiRepository.existsByNgay(ngay)) {
            canhBao.add("Ngày nghỉ");
        }

        if (lichHocRepository.existsByLopHocPhanIdAndNgayHocAndCaHocIdAndTrangThaiNot(
                lopHocPhanId,
                ngay,
                request.getCaHocId(),
                TrangThaiLichHoc.nghi
        )) {
            canhBao.add("Lớp đã có lịch");
        }

        if (lichHocRepository.existsByGiaoVienIdAndNgayHocAndCaHocIdAndTrangThaiNot(
                request.getGiaoVienId(),
                ngay,
                request.getCaHocId(),
                TrangThaiLichHoc.nghi
        )) {
            canhBao.add("Giáo viên bận");
        }

        if (giaoVienKhaDungRepository.existsGiaoVienKhongKhaDung(
                request.getGiaoVienId(),
                thu,
                request.getCaHocId(),
                ngay
        )) {
            canhBao.add("Giáo viên không khả dụng");
        }

        if (lichHocRepository.existsByPhongHocIdAndNgayHocAndCaHocIdAndTrangThaiNot(
                request.getPhongHocId(),
                ngay,
                request.getCaHocId(),
                TrangThaiLichHoc.nghi
        )) {
            canhBao.add("Phòng bận");
        }

        if (lichHocRepository.existsTrungLichSinhVien(
                lopHocPhanId,
                ngay,
                request.getCaHocId(),
                null
        )) {
            canhBao.add("Sinh viên trùng lịch");
        }

        return canhBao;
    }

    private String taoNoiDungBuoiHoc(LopHocPhan lop, int stt, int tongBuoi, Boolean tuDongGanNoiDungSyllabus) {
        if (!Boolean.TRUE.equals(tuDongGanNoiDungSyllabus) || lop.getChuongTrinhMonId() == null) {
            return "Buổi " + stt;
        }

        return syllabusMonHocRepository.findFirstByChuongTrinhMonId(lop.getChuongTrinhMonId())
                .map(syllabus -> {
                    List<SyllabusChuongBai> chuongBai = syllabusChuongBaiRepository
                            .findBySyllabusMonIdOrderByThuTuAsc(syllabus.getId());
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

package org.example.trungcapphuongnam.module.chuongTrinh.validator;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.exception.BadRequestException;
import org.example.trungcapphuongnam.common.exception.DuplicateResourceException;
import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.*;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.ChuongTrinhMon;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.NhomKienThuc;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.NhomTuChon;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.*;
import org.example.trungcapphuongnam.module.daoTao.entity.KhungKy;
import org.example.trungcapphuongnam.module.daoTao.entity.NganhLoaiChuongTrinh;
import org.example.trungcapphuongnam.module.daoTao.repository.KhungKyRepository;
import org.example.trungcapphuongnam.module.daoTao.repository.NganhLoaiChuongTrinhRepository;
import org.example.trungcapphuongnam.module.daoTao.repository.NganhTrinhDoDaoTaoRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ChuongTrinhNghiepVuValidator {

    private final NganhLoaiChuongTrinhRepository nganhLoaiChuongTrinhRepository;
    private final NganhTrinhDoDaoTaoRepository nganhTrinhDoDaoTaoRepository;
    private final SyllabusChuongTrinhGocRepository syllabusChuongTrinhGocRepository;
    private final KhungKyRepository khungKyRepository;

    private final ChuongTrinhRepository chuongTrinhRepository;
    private final ChuongTrinhVersionRepository chuongTrinhVersionRepository;
    private final ChuongTrinhMonRepository chuongTrinhMonRepository;
    private final ChuongTrinhMonQuyDoiDiemMauRepository chuongTrinhMonQuyDoiDiemMauRepository;

    private final MonHocRepository monHocRepository;
    private final NhomKienThucRepository nhomKienThucRepository;
    private final NhomTuChonRepository nhomTuChonRepository;
    private final MonTuChonRepository monTuChonRepository;
    private final MonTienQuyetRepository monTienQuyetRepository;
    private final QuyDoiDiemRepository quyDoiDiemRepository;
    private final QuyDoiDiemMauRepository quyDoiDiemMauRepository;

    private final SyllabusChuongTrinhRepository syllabusChuongTrinhRepository;
    private final SyllabusMonHocRepository syllabusMonHocRepository;
    private final DieuKienMonHocRepository dieuKienMonHocRepository;
    private final SyllabusChuongBaiRepository syllabusChuongBaiRepository;
    private final SyllabusTaiLieuRepository syllabusTaiLieuRepository;

    private final MucTieuChuongTrinhRepository mucTieuChuongTrinhRepository;
    private final NangLucDauRaRepository nangLucDauRaRepository;
    private final ViTriViecLamRepository viTriViecLamRepository;
    private final DieuKienTotNghiepRepository dieuKienTotNghiepRepository;

    private final MucTieuChuongTrinhGocRepository mucTieuChuongTrinhGocRepository;
    private final NangLucDauRaGocRepository nangLucDauRaGocRepository;
    private final ViTriViecLamGocRepository viTriViecLamGocRepository;
    private final DieuKienTotNghiepGocRepository dieuKienTotNghiepGocRepository;

    private final ChuongTrinhVersionMucTieuRepository chuongTrinhVersionMucTieuRepository;
    private final ChuongTrinhVersionNangLucRepository chuongTrinhVersionNangLucRepository;
    private final ChuongTrinhVersionViTriViecLamRepository chuongTrinhVersionViTriViecLamRepository;
    private final ChuongTrinhVersionDieuKienTotNghiepRepository chuongTrinhVersionDieuKienTotNghiepRepository;

    private final DieuKienMonHocGocRepository dieuKienMonHocGocRepository;
    private final TaiLieuGocRepository taiLieuGocRepository;

    private final SyllabusMonHocGocRepository syllabusMonHocGocRepository;
    private final SyllabusMonHocGocChuongBaiRepository syllabusMonHocGocChuongBaiRepository;
    private final SyllabusMonHocGocDieuKienRepository syllabusMonHocGocDieuKienRepository;
    private final SyllabusMonHocGocTaiLieuRepository syllabusMonHocGocTaiLieuRepository;
    private final SyllabusMonHocDieuKienRepository syllabusMonHocDieuKienRepository;
    private final SyllabusMonHocTaiLieuRepository syllabusMonHocTaiLieuRepository;

    private final NhomKienThucGocRepository nhomKienThucGocRepository;
    private final NhomTuChonGocRepository nhomTuChonGocRepository;


    public void validateNhomKienThucGoc(NhomKienThucGocRequest request, Long id) {
        notNull(request, "Dữ liệu nhóm kiến thức gốc không hợp lệ");

        String ma = trimRequired(request.getMa(), "Mã nhóm kiến thức gốc");
        trimRequired(request.getTen(), "Tên nhóm kiến thức gốc");
        String loaiNhom = trimRequired(request.getLoaiNhom(), "Loại nhóm kiến thức gốc");

        if (!List.of("chung", "co_so", "chuyen_mon", "tu_chon").contains(loaiNhom)) {
            throw new BadRequestException("Loại nhóm kiến thức gốc không hợp lệ: " + loaiNhom);
        }

        if (id == null && nhomKienThucGocRepository.existsByMa(ma)) {
            throw new DuplicateResourceException("Mã nhóm kiến thức gốc đã tồn tại: " + ma);
        }

        if (id != null && nhomKienThucGocRepository.existsByMaAndIdNot(ma, id)) {
            throw new DuplicateResourceException("Mã nhóm kiến thức gốc đã tồn tại: " + ma);
        }
    }

    public void validateNhomTuChonGoc(NhomTuChonGocRequest request, Long id) {
        notNull(request, "Dữ liệu nhóm tự chọn gốc không hợp lệ");

        String ma = trimRequired(request.getMa(), "Mã nhóm tự chọn gốc");
        trimRequired(request.getTen(), "Tên nhóm tự chọn gốc");

        if (id == null && nhomTuChonGocRepository.existsByMa(ma)) {
            throw new DuplicateResourceException("Mã nhóm tự chọn gốc đã tồn tại: " + ma);
        }

        if (id != null && nhomTuChonGocRepository.existsByMaAndIdNot(ma, id)) {
            throw new DuplicateResourceException("Mã nhóm tự chọn gốc đã tồn tại: " + ma);
        }
    }
    public void validateChuongTrinh(ChuongTrinhRequest request, Long id) {
        notNull(request, "Dữ liệu chương trình không hợp lệ");

        Long nganhLoaiChuongTrinhId = requireId(request.getNganhLoaiChuongTrinhId(), "nganhLoaiChuongTrinhId");

        String ma = trimRequired(request.getMaChuongTrinh(), "Mã chương trình");
        trimRequired(request.getTenChuongTrinh(), "Tên chương trình");

        NganhLoaiChuongTrinh nganhLoai = nganhLoaiChuongTrinhRepository.findById(nganhLoaiChuongTrinhId)
                .orElseThrow(() -> new ResourceNotFoundException("Loại chương trình theo ngành không tồn tại: " + nganhLoaiChuongTrinhId));

        if (request.getTrinhDoId() != null) {
            Long trinhDoId = request.getTrinhDoId();
            boolean daGanTrinhDoVaoNganh = nganhTrinhDoDaoTaoRepository.existsByNganhIdAndTrinhDoId(
                    nganhLoai.getNganhId(),
                    trinhDoId
            );

            if (!daGanTrinhDoVaoNganh) {
                throw new ResourceNotFoundException("Trình độ đào tạo chưa được gán vào ngành này: " + trinhDoId);
            }
        }

        if (id == null && chuongTrinhRepository.existsByMaChuongTrinh(ma)) {
            throw new DuplicateResourceException("Mã chương trình đã tồn tại: " + ma);
        }

        if (id != null && chuongTrinhRepository.existsByMaChuongTrinhAndIdNot(ma, id)) {
            throw new DuplicateResourceException("Mã chương trình đã tồn tại: " + ma);
        }
    }
    public void validateSyllabusChuongTrinhGoc(SyllabusChuongTrinhGocRequest request, Long id) {
        notNull(request, "Dữ liệu syllabus chương trình gốc không hợp lệ");

        String ma = trimRequired(request.getMa(), "Mã syllabus chương trình gốc");
        trimRequired(request.getTen(), "Tên syllabus chương trình gốc");

        if (id == null && syllabusChuongTrinhGocRepository.existsByMa(ma)) {
            throw new DuplicateResourceException("Mã syllabus chương trình gốc đã tồn tại: " + ma);
        }

        if (id != null && syllabusChuongTrinhGocRepository.existsByMaAndIdNot(ma, id)) {
            throw new DuplicateResourceException("Mã syllabus chương trình gốc đã tồn tại: " + ma);
        }
    }
    public void validateChuongTrinhVersion(ChuongTrinhVersionRequest request, Long id) {
        notNull(request, "Dữ liệu phiên bản chương trình không hợp lệ");

        Long chuongTrinhId = requireId(request.getChuongTrinhId(), "chuongTrinhId");
        String maVersion = trimRequired(request.getMaVersion(), "Mã phiên bản");
        trimRequired(request.getTenVersion(), "Tên phiên bản");

        requireExists(chuongTrinhRepository, chuongTrinhId, "Chương trình");

        if (request.getNgayApDung() != null
                && request.getNgayHetHieuLuc() != null
                && request.getNgayHetHieuLuc().isBefore(request.getNgayApDung())) {
            throw new BadRequestException("Ngày hết hiệu lực không được nhỏ hơn ngày áp dụng");
        }

        if (request.getNgayQuyetDinh() != null
                && request.getNgayApDung() != null
                && request.getNgayQuyetDinh().isAfter(request.getNgayApDung())) {
            throw new BadRequestException("Ngày quyết định không được sau ngày áp dụng");
        }

        positiveOrZero(request.getTongTinChi(), "Tổng tín chỉ");
        positiveOrZero(request.getTongSoGio(), "Tổng số giờ");
        positiveOrZero(request.getTongGioLyThuyet(), "Tổng giờ lý thuyết");
        positiveOrZero(request.getTongGioThucHanh(), "Tổng giờ thực hành");
        positiveOrZero(request.getTongGioKiemTra(), "Tổng giờ kiểm tra");

        checkTotalEqualsParts(
                request.getTongSoGio(),
                request.getTongGioLyThuyet(),
                request.getTongGioThucHanh(),
                request.getTongGioKiemTra(),
                "Tổng số giờ của version"
        );

        if (id == null && chuongTrinhVersionRepository.existsByChuongTrinhIdAndMaVersion(chuongTrinhId, maVersion)) {
            throw new DuplicateResourceException("Mã phiên bản đã tồn tại trong chương trình: " + maVersion);
        }

        if (id != null && chuongTrinhVersionRepository.existsByChuongTrinhIdAndMaVersionAndIdNot(chuongTrinhId, maVersion, id)) {
            throw new DuplicateResourceException("Mã phiên bản đã tồn tại trong chương trình: " + maVersion);
        }


    }
    public void validateSyllabusMonHocDieuKien(SyllabusMonHocDieuKienRequest request, Long id) {
        notNull(request, "Dữ liệu gán điều kiện môn học vào syllabus không hợp lệ");

        Long syllabusMonId = requireId(request.getSyllabusMonId(), "syllabusMonId");
        Long dieuKienGocId = requireId(request.getDieuKienGocId(), "dieuKienGocId");

        requireExists(syllabusMonHocRepository, syllabusMonId, "Syllabus môn học");
        requireExists(dieuKienMonHocGocRepository, dieuKienGocId, "Điều kiện môn học gốc");

        positiveOrZeroInt(request.getThuTu(), "Thứ tự điều kiện môn học");

        if (id == null && syllabusMonHocDieuKienRepository.existsBySyllabusMonIdAndDieuKienGocId(
                syllabusMonId,
                dieuKienGocId
        )) {
            throw new DuplicateResourceException("Điều kiện môn học gốc đã được gán cho syllabus này");
        }

        if (id != null && syllabusMonHocDieuKienRepository.existsBySyllabusMonIdAndDieuKienGocIdAndIdNot(
                syllabusMonId,
                dieuKienGocId,
                id
        )) {
            throw new DuplicateResourceException("Điều kiện môn học gốc đã được gán cho syllabus này");
        }

        if (request.getThuTu() != null) {
            if (id == null && syllabusMonHocDieuKienRepository.existsBySyllabusMonIdAndThuTu(
                    syllabusMonId,
                    request.getThuTu()
            )) {
                throw new DuplicateResourceException("Thứ tự điều kiện môn học đã tồn tại trong syllabus: " + request.getThuTu());
            }

            if (id != null && syllabusMonHocDieuKienRepository.existsBySyllabusMonIdAndThuTuAndIdNot(
                    syllabusMonId,
                    request.getThuTu(),
                    id
            )) {
                throw new DuplicateResourceException("Thứ tự điều kiện môn học đã tồn tại trong syllabus: " + request.getThuTu());
            }
        }
    }
    public void validateSyllabusMonHocTaiLieu(SyllabusMonHocTaiLieuRequest request, Long id) {
        notNull(request, "Dữ liệu gán tài liệu gốc vào syllabus không hợp lệ");

        Long syllabusMonId = requireId(request.getSyllabusMonId(), "syllabusMonId");
        Long taiLieuGocId = requireId(request.getTaiLieuGocId(), "taiLieuGocId");

        requireExists(syllabusMonHocRepository, syllabusMonId, "Syllabus môn học");
        requireExists(taiLieuGocRepository, taiLieuGocId, "Tài liệu gốc");

        positiveOrZeroInt(request.getThuTu(), "Thứ tự tài liệu syllabus");

        if (id == null && syllabusMonHocTaiLieuRepository.existsBySyllabusMonIdAndTaiLieuGocId(
                syllabusMonId,
                taiLieuGocId
        )) {
            throw new DuplicateResourceException("Tài liệu gốc đã được gán cho syllabus này");
        }

        if (id != null && syllabusMonHocTaiLieuRepository.existsBySyllabusMonIdAndTaiLieuGocIdAndIdNot(
                syllabusMonId,
                taiLieuGocId,
                id
        )) {
            throw new DuplicateResourceException("Tài liệu gốc đã được gán cho syllabus này");
        }

        if (request.getThuTu() != null) {
            if (id == null && syllabusMonHocTaiLieuRepository.existsBySyllabusMonIdAndThuTu(
                    syllabusMonId,
                    request.getThuTu()
            )) {
                throw new DuplicateResourceException("Thứ tự tài liệu đã tồn tại trong syllabus: " + request.getThuTu());
            }

            if (id != null && syllabusMonHocTaiLieuRepository.existsBySyllabusMonIdAndThuTuAndIdNot(
                    syllabusMonId,
                    request.getThuTu(),
                    id
            )) {
                throw new DuplicateResourceException("Thứ tự tài liệu đã tồn tại trong syllabus: " + request.getThuTu());
            }
        }
    }
    public void validateChuongTrinhMon(ChuongTrinhMonRequest request, Long id) {
        notNull(request, "Dữ liệu môn trong chương trình không hợp lệ");

        Long versionId = requireId(request.getChuongTrinhVersionId(), "chuongTrinhVersionId");
        Long monHocId = requireId(request.getMonHocId(), "monHocId");
        Long khungKyId = requireId(request.getKhungKyId(), "khungKyId");

        String maMonTrongCt = trimRequired(request.getMaMonTrongCt(), "Mã môn trong chương trình");
        trimRequired(request.getLoai(), "Loại môn trong chương trình");
        trimRequired(request.getLoaiHocPhan(), "Loại học phần");

        requireExists(chuongTrinhVersionRepository, versionId, "Phiên bản chương trình");
        requireExists(monHocRepository, monHocId, "Môn học");

        KhungKy khungKy = khungKyRepository.findById(khungKyId)
                .orElseThrow(() -> new ResourceNotFoundException("Khung kỳ không tồn tại: " + khungKyId));

        if (!versionId.equals(khungKy.getChuongTrinhVersionId())) {
            throw new BadRequestException("Khung kỳ không thuộc đúng phiên bản chương trình đang chọn");
        }

        if (request.getNhomKienThucId() != null) {
            NhomKienThuc nhom = nhomKienThucRepository.findById(request.getNhomKienThucId())
                    .orElseThrow(() -> new ResourceNotFoundException("Nhóm kiến thức không tồn tại: " + request.getNhomKienThucId()));

            if (!versionId.equals(nhom.getChuongTrinhVersionId())) {
                throw new BadRequestException("Nhóm kiến thức không thuộc đúng phiên bản chương trình đang chọn");
            }
        }

        positiveOrZeroInt(request.getThuTu(), "Thứ tự môn trong kỳ");
        positiveOrZero(request.getSoTinChi(), "Số tín chỉ");
        positiveOrZero(request.getTongGio(), "Tổng giờ");
        positiveOrZero(request.getGioLyThuyet(), "Giờ lý thuyết");
        positiveOrZero(request.getGioThucHanh(), "Giờ thực hành");
        positiveOrZero(request.getGioKiemTra(), "Giờ kiểm tra");

        checkTotalEqualsParts(
                request.getTongGio(),
                request.getGioLyThuyet(),
                request.getGioThucHanh(),
                request.getGioKiemTra(),
                "Tổng giờ môn trong kỳ"
        );

        if (id == null && chuongTrinhMonRepository.existsByChuongTrinhVersionIdAndKhungKyIdAndMaMonTrongCt(versionId, khungKyId, maMonTrongCt)) {
            throw new DuplicateResourceException("Mã môn trong chương trình đã tồn tại trong kỳ này: " + maMonTrongCt);
        }

        if (id != null && chuongTrinhMonRepository.existsByChuongTrinhVersionIdAndKhungKyIdAndMaMonTrongCtAndIdNot(versionId, khungKyId, maMonTrongCt, id)) {
            throw new DuplicateResourceException("Mã môn trong chương trình đã tồn tại trong kỳ này: " + maMonTrongCt);
        }

        if (id == null && chuongTrinhMonRepository.existsByChuongTrinhVersionIdAndMonHocId(versionId, monHocId)) {
            throw new DuplicateResourceException("Môn học này đã được thêm vào version hiện tại rồi");
        }

        if (id != null && chuongTrinhMonRepository.existsByChuongTrinhVersionIdAndMonHocIdAndIdNot(versionId, monHocId, id)) {
            throw new DuplicateResourceException("Môn học này đã được thêm vào version hiện tại rồi");
        }

        if (request.getThuTu() != null) {
            if (id == null && chuongTrinhMonRepository.existsByChuongTrinhVersionIdAndKhungKyIdAndThuTu(versionId, khungKyId, request.getThuTu())) {
                throw new DuplicateResourceException("Thứ tự môn đã tồn tại trong kỳ đang chọn: " + request.getThuTu());
            }

            if (id != null && chuongTrinhMonRepository.existsByChuongTrinhVersionIdAndKhungKyIdAndThuTuAndIdNot(versionId, khungKyId, request.getThuTu(), id)) {
                throw new DuplicateResourceException("Thứ tự môn đã tồn tại trong kỳ đang chọn: " + request.getThuTu());
            }
        }
    }

    public void validateNhomKienThuc(NhomKienThucRequest request, Long id) {
        notNull(request, "Dữ liệu nhóm kiến thức không hợp lệ");

        Long versionId = requireId(request.getChuongTrinhVersionId(), "chuongTrinhVersionId");
        String ma = trimRequired(request.getMa(), "Mã nhóm kiến thức");
        trimRequired(request.getTen(), "Tên nhóm kiến thức");

        requireExists(chuongTrinhVersionRepository, versionId, "Phiên bản chương trình");

        positiveOrZero(request.getTongTinChi(), "Tổng tín chỉ");
        positiveOrZero(request.getTongSoGio(), "Tổng số giờ");
        positiveOrZero(request.getTongGioLyThuyet(), "Tổng giờ lý thuyết");
        positiveOrZero(request.getTongGioThucHanh(), "Tổng giờ thực hành");
        positiveOrZero(request.getTongGioKiemTra(), "Tổng giờ kiểm tra");
        positiveOrZeroInt(request.getThuTu(), "Thứ tự nhóm kiến thức");

        checkTotalEqualsParts(
                request.getTongSoGio(),
                request.getTongGioLyThuyet(),
                request.getTongGioThucHanh(),
                request.getTongGioKiemTra(),
                "Tổng số giờ nhóm kiến thức"
        );

        if (id == null && nhomKienThucRepository.existsByChuongTrinhVersionIdAndMa(versionId, ma)) {
            throw new DuplicateResourceException("Mã nhóm kiến thức đã tồn tại trong version: " + ma);
        }

        if (id != null && nhomKienThucRepository.existsByChuongTrinhVersionIdAndMaAndIdNot(versionId, ma, id)) {
            throw new DuplicateResourceException("Mã nhóm kiến thức đã tồn tại trong version: " + ma);
        }

        if (request.getThuTu() != null) {
            if (id == null && nhomKienThucRepository.existsByChuongTrinhVersionIdAndThuTu(versionId, request.getThuTu())) {
                throw new DuplicateResourceException("Thứ tự nhóm kiến thức đã tồn tại trong version: " + request.getThuTu());
            }

            if (id != null && nhomKienThucRepository.existsByChuongTrinhVersionIdAndThuTuAndIdNot(versionId, request.getThuTu(), id)) {
                throw new DuplicateResourceException("Thứ tự nhóm kiến thức đã tồn tại trong version: " + request.getThuTu());
            }
        }
        if (request.getNhomKienThucGocId() != null) {
            requireExists(
                    nhomKienThucGocRepository,
                    request.getNhomKienThucGocId(),
                    "Nhóm kiến thức gốc"
            );
        }
        if (request.getNhomKienThucGocId() != null) {
            Long gocId = request.getNhomKienThucGocId();

            if (id == null && nhomKienThucRepository.existsByChuongTrinhVersionIdAndNhomKienThucGocId(versionId, gocId)) {
                throw new DuplicateResourceException("Nhóm kiến thức gốc đã được gán vào version này");
            }

            if (id != null && nhomKienThucRepository.existsByChuongTrinhVersionIdAndNhomKienThucGocIdAndIdNot(versionId, gocId, id)) {
                throw new DuplicateResourceException("Nhóm kiến thức gốc đã được gán vào version này");
            }
        }
    }

    public void validateNhomTuChon(NhomTuChonRequest request, Long id) {
        notNull(request, "Dữ liệu nhóm tự chọn không hợp lệ");

        Long versionId = requireId(request.getChuongTrinhVersionId(), "chuongTrinhVersionId");
        String ten = trimRequired(request.getTen(), "Tên nhóm tự chọn");

        requireExists(chuongTrinhVersionRepository, versionId, "Phiên bản chương trình");

        positiveOrZeroInt(request.getSoMonChon(), "Số môn chọn");
        positiveOrZero(request.getSoTinChiCanDat(), "Số tín chỉ cần đạt");

        if (request.getSoMonChon() != null && request.getSoMonChon() == 0) {
            throw new BadRequestException("Số môn chọn phải lớn hơn 0");
        }

        if (request.getSoTinChiCanDat() != null && request.getSoTinChiCanDat().compareTo(BigDecimal.ZERO) == 0) {
            throw new BadRequestException("Số tín chỉ cần đạt phải lớn hơn 0");
        }

        if (id == null && nhomTuChonRepository.existsByChuongTrinhVersionIdAndTen(versionId, ten)) {
            throw new DuplicateResourceException("Tên nhóm tự chọn đã tồn tại trong version: " + ten);
        }

        if (id != null && nhomTuChonRepository.existsByChuongTrinhVersionIdAndTenAndIdNot(versionId, ten, id)) {
            throw new DuplicateResourceException("Tên nhóm tự chọn đã tồn tại trong version: " + ten);
        }
        if (request.getNhomTuChonGocId() != null) {
            requireExists(
                    nhomTuChonGocRepository,
                    request.getNhomTuChonGocId(),
                    "Nhóm tự chọn gốc"
            );
        }
        if (request.getNhomTuChonGocId() != null) {
            Long gocId = request.getNhomTuChonGocId();

            if (id == null && nhomTuChonRepository.existsByChuongTrinhVersionIdAndNhomTuChonGocId(versionId, gocId)) {
                throw new DuplicateResourceException("Nhóm tự chọn gốc đã được gán vào version này");
            }

            if (id != null && nhomTuChonRepository.existsByChuongTrinhVersionIdAndNhomTuChonGocIdAndIdNot(versionId, gocId, id)) {
                throw new DuplicateResourceException("Nhóm tự chọn gốc đã được gán vào version này");
            }
        }
    }

    public void validateMonHoc(MonHocRequest request, Long id) {
        notNull(request, "Dữ liệu môn học không hợp lệ");

        String ma = trimRequired(request.getMaMon(), "Mã môn học");
        trimRequired(request.getTenMon(), "Tên môn học");

        if (id == null && monHocRepository.existsByMaMon(ma)) {
            throw new DuplicateResourceException("Mã môn học đã tồn tại: " + ma);
        }

        if (id != null && monHocRepository.existsByMaMonAndIdNot(ma, id)) {
            throw new DuplicateResourceException("Mã môn học đã tồn tại: " + ma);
        }
    }

    public void validateMonTuChon(MonTuChonRequest request, Long id) {
        notNull(request, "Dữ liệu môn tự chọn không hợp lệ");

        Long nhomId = requireId(request.getNhomId(), "nhomId");
        Long chuongTrinhMonId = requireId(request.getChuongTrinhMonId(), "chuongTrinhMonId");

        NhomTuChon nhom = nhomTuChonRepository.findById(nhomId)
                .orElseThrow(() -> new ResourceNotFoundException("Nhóm tự chọn không tồn tại: " + nhomId));

        ChuongTrinhMon mon = chuongTrinhMonRepository.findById(chuongTrinhMonId)
                .orElseThrow(() -> new ResourceNotFoundException("Môn trong chương trình không tồn tại: " + chuongTrinhMonId));

        if (!nhom.getChuongTrinhVersionId().equals(mon.getChuongTrinhVersionId())) {
            throw new BadRequestException("Môn tự chọn phải thuộc cùng version với nhóm tự chọn");
        }

        if (id == null && monTuChonRepository.existsByNhomIdAndChuongTrinhMonId(nhomId, chuongTrinhMonId)) {
            throw new DuplicateResourceException("Môn này đã có trong nhóm tự chọn");
        }

        if (id != null && monTuChonRepository.existsByNhomIdAndChuongTrinhMonIdAndIdNot(nhomId, chuongTrinhMonId, id)) {
            throw new DuplicateResourceException("Môn này đã có trong nhóm tự chọn");
        }
    }

    public void validateMonTienQuyet(MonTienQuyetRequest request, Long id) {
        notNull(request, "Dữ liệu môn tiên quyết không hợp lệ");

        Long monId = requireId(request.getMonId(), "monId");
        Long monDieuKienId = requireId(request.getMonDieuKienId(), "monDieuKienId");

        trimRequired(request.getLoai(), "Loại điều kiện môn");

        if (monId.equals(monDieuKienId)) {
            throw new BadRequestException("Một môn không được tự làm môn tiên quyết của chính nó");
        }

        ChuongTrinhMon mon = chuongTrinhMonRepository.findById(monId)
                .orElseThrow(() -> new ResourceNotFoundException("Môn trong chương trình không tồn tại: " + monId));

        ChuongTrinhMon dieuKien = chuongTrinhMonRepository.findById(monDieuKienId)
                .orElseThrow(() -> new ResourceNotFoundException("Môn điều kiện không tồn tại: " + monDieuKienId));

        if (!mon.getChuongTrinhVersionId().equals(dieuKien.getChuongTrinhVersionId())) {
            throw new BadRequestException("Môn tiên quyết phải thuộc cùng version chương trình");
        }

        if (id == null && monTienQuyetRepository.existsByMonIdAndMonDieuKienId(monId, monDieuKienId)) {
            throw new DuplicateResourceException("Điều kiện môn tiên quyết đã tồn tại");
        }

        if (id != null && monTienQuyetRepository.existsByMonIdAndMonDieuKienIdAndIdNot(monId, monDieuKienId, id)) {
            throw new DuplicateResourceException("Điều kiện môn tiên quyết đã tồn tại");
        }

        if (monTienQuyetRepository.existsByMonIdAndMonDieuKienId(monDieuKienId, monId)) {
            throw new BadRequestException("Không được tạo điều kiện môn tiên quyết vòng ngược trực tiếp");
        }
    }

    public void validateQuyDoiDiem(QuyDoiDiemRequest request, Long id) {
        notNull(request, "Dữ liệu quy đổi điểm không hợp lệ");

        Long chuongTrinhMonId = requireId(request.getChuongTrinhMonId(), "chuongTrinhMonId");

        requireExists(chuongTrinhMonRepository, chuongTrinhMonId, "Môn trong chương trình");
        validateNguongDiem(request.getNguongTu(), request.getNguongDen(), request.getDiemQuyDoi());

        if (request.getNguongTu() != null
                && request.getNguongDen() != null
                && quyDoiDiemRepository.existsOverlap(chuongTrinhMonId, request.getNguongTu(), request.getNguongDen(), id)) {
            throw new DuplicateResourceException("Khoảng ngưỡng quy đổi điểm bị chồng lấn với dòng đã có của môn này");
        }
    }

    public void validateSyllabusMonHoc(SyllabusMonHocRequest request, Long id) {
        notNull(request, "Dữ liệu syllabus môn học không hợp lệ");

        Long chuongTrinhMonId = requireId(request.getChuongTrinhMonId(), "chuongTrinhMonId");

        requireExists(chuongTrinhMonRepository, chuongTrinhMonId, "Môn trong chương trình");

        validateDiemTrongKhoang(request.getDiemDatToiThieu(), "Điểm đạt tối thiểu", BigDecimal.ZERO, BigDecimal.TEN);
        validateDiemTrongKhoang(request.getTyLeChuyenCanToiThieu(), "Tỷ lệ chuyên cần tối thiểu", BigDecimal.ZERO, new BigDecimal("100"));

        if (id == null && syllabusMonHocRepository.existsByChuongTrinhMonId(chuongTrinhMonId)) {
            throw new DuplicateResourceException("Môn trong chương trình này đã có syllabus môn học");
        }

        if (id != null && syllabusMonHocRepository.existsByChuongTrinhMonIdAndIdNot(chuongTrinhMonId, id)) {
            throw new DuplicateResourceException("Môn trong chương trình này đã có syllabus môn học");
        }
        if (request.getSyllabusMonHocGocId() != null) {
            requireExists(
                    syllabusMonHocGocRepository,
                    request.getSyllabusMonHocGocId(),
                    "Syllabus môn học gốc"
            );
        }
    }

    public void validateDieuKienMonHocGoc(DieuKienMonHocGocRequest request, Long id) {
        notNull(request, "Dữ liệu điều kiện môn học gốc không hợp lệ");

        String ma = trimRequired(request.getMa(), "Mã điều kiện môn học gốc");

        if (request.getLoai() == null) {
            throw new BadRequestException("Loại điều kiện môn học gốc không được để trống");
        }

        trimRequired(request.getNoiDung(), "Nội dung điều kiện môn học gốc");

        if (id == null && dieuKienMonHocGocRepository.existsByMa(ma)) {
            throw new DuplicateResourceException("Mã điều kiện môn học gốc đã tồn tại: " + ma);
        }

        if (id != null && dieuKienMonHocGocRepository.existsByMaAndIdNot(ma, id)) {
            throw new DuplicateResourceException("Mã điều kiện môn học gốc đã tồn tại: " + ma);
        }
    }

    public void validateTaiLieuGoc(TaiLieuGocRequest request, Long id) {
        notNull(request, "Dữ liệu tài liệu gốc không hợp lệ");

        String ma = trimRequired(request.getMa(), "Mã tài liệu gốc");
        trimRequired(request.getTen(), "Tên tài liệu gốc");

        if (request.getNamXuatBan() != null) {
            int currentYear = LocalDate.now().getYear();
            if (request.getNamXuatBan() < 1900 || request.getNamXuatBan() > currentYear + 1) {
                throw new BadRequestException("Năm xuất bản tài liệu không hợp lệ");
            }
        }

        if (id == null && taiLieuGocRepository.existsByMa(ma)) {
            throw new DuplicateResourceException("Mã tài liệu gốc đã tồn tại: " + ma);
        }

        if (id != null && taiLieuGocRepository.existsByMaAndIdNot(ma, id)) {
            throw new DuplicateResourceException("Mã tài liệu gốc đã tồn tại: " + ma);
        }
    }
    public void validateSyllabusMonHocGoc(SyllabusMonHocGocRequest request, Long id) {
        notNull(request, "Dữ liệu syllabus môn học gốc không hợp lệ");

        Long monHocId = requireId(request.getMonHocId(), "monHocId");
        String ma = trimRequired(request.getMa(), "Mã syllabus môn học gốc");
        String ten = trimRequired(request.getTen(), "Tên syllabus môn học gốc");

        requireExists(monHocRepository, monHocId, "Môn học");

        validateDiemTrongKhoang(
                request.getDiemDatToiThieu(),
                "Điểm đạt tối thiểu",
                BigDecimal.ZERO,
                BigDecimal.TEN
        );

        validateDiemTrongKhoang(
                request.getTyLeChuyenCanToiThieu(),
                "Tỷ lệ chuyên cần tối thiểu",
                BigDecimal.ZERO,
                new BigDecimal("100")
        );

        if (id == null && syllabusMonHocGocRepository.existsByMa(ma)) {
            throw new DuplicateResourceException("Mã syllabus môn học gốc đã tồn tại: " + ma);
        }

        if (id != null && syllabusMonHocGocRepository.existsByMaAndIdNot(ma, id)) {
            throw new DuplicateResourceException("Mã syllabus môn học gốc đã tồn tại: " + ma);
        }

        if (id == null && syllabusMonHocGocRepository.existsByMonHocIdAndTen(monHocId, ten)) {
            throw new DuplicateResourceException("Tên syllabus gốc đã tồn tại trong môn học này: " + ten);
        }

        if (id != null && syllabusMonHocGocRepository.existsByMonHocIdAndTenAndIdNot(monHocId, ten, id)) {
            throw new DuplicateResourceException("Tên syllabus gốc đã tồn tại trong môn học này: " + ten);
        }
    }
    public void validateSyllabusMonHocGocChuongBai(SyllabusMonHocGocChuongBaiRequest request, Long id) {
        notNull(request, "Dữ liệu chương bài syllabus gốc không hợp lệ");

        Long syllabusGocId = requireId(request.getSyllabusMonHocGocId(), "syllabusMonHocGocId");

        trimRequired(request.getTen(), "Tên chương bài syllabus gốc");
        requireExists(syllabusMonHocGocRepository, syllabusGocId, "Syllabus môn học gốc");

        positiveOrZero(request.getTongGio(), "Tổng giờ chương bài gốc");
        positiveOrZero(request.getGioLyThuyet(), "Giờ lý thuyết chương bài gốc");
        positiveOrZero(request.getGioThucHanh(), "Giờ thực hành chương bài gốc");
        positiveOrZero(request.getGioKiemTra(), "Giờ kiểm tra chương bài gốc");
        positiveOrZeroInt(request.getThuTu(), "Thứ tự chương bài gốc");

        checkTotalEqualsParts(
                request.getTongGio(),
                request.getGioLyThuyet(),
                request.getGioThucHanh(),
                request.getGioKiemTra(),
                "Tổng giờ chương bài syllabus gốc"
        );

        if (request.getThuTu() != null) {
            if (id == null && syllabusMonHocGocChuongBaiRepository.existsBySyllabusMonHocGocIdAndThuTu(
                    syllabusGocId,
                    request.getThuTu()
            )) {
                throw new DuplicateResourceException("Thứ tự chương bài gốc đã tồn tại trong syllabus gốc");
            }

            if (id != null && syllabusMonHocGocChuongBaiRepository.existsBySyllabusMonHocGocIdAndThuTuAndIdNot(
                    syllabusGocId,
                    request.getThuTu(),
                    id
            )) {
                throw new DuplicateResourceException("Thứ tự chương bài gốc đã tồn tại trong syllabus gốc");
            }
        }
    }

    public void validateSyllabusMonHocGocDieuKien(SyllabusMonHocGocDieuKienRequest request, Long id) {
        notNull(request, "Dữ liệu gán điều kiện vào syllabus gốc không hợp lệ");

        Long syllabusGocId = requireId(request.getSyllabusMonHocGocId(), "syllabusMonHocGocId");
        Long dieuKienGocId = requireId(request.getDieuKienGocId(), "dieuKienGocId");

        requireExists(syllabusMonHocGocRepository, syllabusGocId, "Syllabus môn học gốc");
        requireExists(dieuKienMonHocGocRepository, dieuKienGocId, "Điều kiện môn học gốc");

        positiveOrZeroInt(request.getThuTu(), "Thứ tự điều kiện trong syllabus gốc");

        if (id == null && syllabusMonHocGocDieuKienRepository.existsBySyllabusMonHocGocIdAndDieuKienGocId(
                syllabusGocId,
                dieuKienGocId
        )) {
            throw new DuplicateResourceException("Điều kiện gốc đã được gán cho syllabus gốc này");
        }

        if (id != null && syllabusMonHocGocDieuKienRepository.existsBySyllabusMonHocGocIdAndDieuKienGocIdAndIdNot(
                syllabusGocId,
                dieuKienGocId,
                id
        )) {
            throw new DuplicateResourceException("Điều kiện gốc đã được gán cho syllabus gốc này");
        }

        if (request.getThuTu() != null) {
            if (id == null && syllabusMonHocGocDieuKienRepository.existsBySyllabusMonHocGocIdAndThuTu(
                    syllabusGocId,
                    request.getThuTu()
            )) {
                throw new DuplicateResourceException("Thứ tự điều kiện đã tồn tại trong syllabus gốc");
            }

            if (id != null && syllabusMonHocGocDieuKienRepository.existsBySyllabusMonHocGocIdAndThuTuAndIdNot(
                    syllabusGocId,
                    request.getThuTu(),
                    id
            )) {
                throw new DuplicateResourceException("Thứ tự điều kiện đã tồn tại trong syllabus gốc");
            }
        }
    }
    public void validateSyllabusMonHocGocTaiLieu(SyllabusMonHocGocTaiLieuRequest request, Long id) {
        notNull(request, "Dữ liệu gán tài liệu vào syllabus gốc không hợp lệ");

        Long syllabusGocId = requireId(request.getSyllabusMonHocGocId(), "syllabusMonHocGocId");
        Long taiLieuGocId = requireId(request.getTaiLieuGocId(), "taiLieuGocId");

        requireExists(syllabusMonHocGocRepository, syllabusGocId, "Syllabus môn học gốc");
        requireExists(taiLieuGocRepository, taiLieuGocId, "Tài liệu gốc");

        positiveOrZeroInt(request.getThuTu(), "Thứ tự tài liệu trong syllabus gốc");

        if (id == null && syllabusMonHocGocTaiLieuRepository.existsBySyllabusMonHocGocIdAndTaiLieuGocId(
                syllabusGocId,
                taiLieuGocId
        )) {
            throw new DuplicateResourceException("Tài liệu gốc đã được gán cho syllabus gốc này");
        }

        if (id != null && syllabusMonHocGocTaiLieuRepository.existsBySyllabusMonHocGocIdAndTaiLieuGocIdAndIdNot(
                syllabusGocId,
                taiLieuGocId,
                id
        )) {
            throw new DuplicateResourceException("Tài liệu gốc đã được gán cho syllabus gốc này");
        }

        if (request.getThuTu() != null) {
            if (id == null && syllabusMonHocGocTaiLieuRepository.existsBySyllabusMonHocGocIdAndThuTu(
                    syllabusGocId,
                    request.getThuTu()
            )) {
                throw new DuplicateResourceException("Thứ tự tài liệu đã tồn tại trong syllabus gốc");
            }

            if (id != null && syllabusMonHocGocTaiLieuRepository.existsBySyllabusMonHocGocIdAndThuTuAndIdNot(
                    syllabusGocId,
                    request.getThuTu(),
                    id
            )) {
                throw new DuplicateResourceException("Thứ tự tài liệu đã tồn tại trong syllabus gốc");
            }
        }
    }

    public void validateSyllabusChuongTrinh(SyllabusChuongTrinhRequest request, Long id) {
        notNull(request, "Dữ liệu syllabus chương trình không hợp lệ");

        Long versionId = requireId(request.getChuongTrinhVersionId(), "chuongTrinhVersionId");

        requireExists(chuongTrinhVersionRepository, versionId, "Phiên bản chương trình");

        if (request.getSyllabusChuongTrinhGocId() != null) {
            requireExists(
                    syllabusChuongTrinhGocRepository,
                    request.getSyllabusChuongTrinhGocId(),
                    "Syllabus chương trình gốc"
            );
        }

        if (id == null && syllabusChuongTrinhRepository.existsByChuongTrinhVersionId(versionId)) {
            throw new DuplicateResourceException("Phiên bản chương trình này đã có syllabus chương trình");
        }

        if (id != null && syllabusChuongTrinhRepository.existsByChuongTrinhVersionIdAndIdNot(versionId, id)) {
            throw new DuplicateResourceException("Phiên bản chương trình này đã có syllabus chương trình");
        }

        if (request.getSyllabusChuongTrinhGocId() != null) {
            Long gocId = request.getSyllabusChuongTrinhGocId();

            if (id == null && syllabusChuongTrinhRepository.existsByChuongTrinhVersionIdAndSyllabusChuongTrinhGocId(versionId, gocId)) {
                throw new DuplicateResourceException("Syllabus chương trình gốc đã được áp dụng cho version này");
            }

            if (id != null && syllabusChuongTrinhRepository.existsByChuongTrinhVersionIdAndSyllabusChuongTrinhGocIdAndIdNot(versionId, gocId, id)) {
                throw new DuplicateResourceException("Syllabus chương trình gốc đã được áp dụng cho version này");
            }
        }
    }

    public void validateSyllabusChuongBai(SyllabusChuongBaiRequest request, Long id) {
        notNull(request, "Dữ liệu chương bài syllabus không hợp lệ");

        Long syllabusMonId = requireId(request.getSyllabusMonId(), "syllabusMonId");

        trimRequired(request.getTen(), "Tên chương bài");
        requireExists(syllabusMonHocRepository, syllabusMonId, "Syllabus môn học");

        positiveOrZero(request.getTongGio(), "Tổng giờ chương bài");
        positiveOrZero(request.getGioLyThuyet(), "Giờ lý thuyết chương bài");
        positiveOrZero(request.getGioThucHanh(), "Giờ thực hành chương bài");
        positiveOrZero(request.getGioKiemTra(), "Giờ kiểm tra chương bài");
        positiveOrZeroInt(request.getThuTu(), "Thứ tự chương bài");

        checkTotalEqualsParts(
                request.getTongGio(),
                request.getGioLyThuyet(),
                request.getGioThucHanh(),
                request.getGioKiemTra(),
                "Tổng giờ chương bài"
        );

        if (request.getThuTu() != null) {
            if (id == null && syllabusChuongBaiRepository.existsBySyllabusMonIdAndThuTu(syllabusMonId, request.getThuTu())) {
                throw new DuplicateResourceException("Thứ tự chương bài đã tồn tại trong syllabus môn");
            }

            if (id != null && syllabusChuongBaiRepository.existsBySyllabusMonIdAndThuTuAndIdNot(syllabusMonId, request.getThuTu(), id)) {
                throw new DuplicateResourceException("Thứ tự chương bài đã tồn tại trong syllabus môn");
            }
        }
    }

    public void validateSyllabusTaiLieu(SyllabusTaiLieuRequest request, Long id) {
        notNull(request, "Dữ liệu tài liệu syllabus không hợp lệ");

        Long syllabusMonId = requireId(request.getSyllabusMonId(), "syllabusMonId");
        String ten = trimRequired(request.getTen(), "Tên tài liệu");

        requireExists(syllabusMonHocRepository, syllabusMonId, "Syllabus môn học");

        if (request.getNamXuatBan() != null) {
            int currentYear = LocalDate.now().getYear();
            if (request.getNamXuatBan() < 1900 || request.getNamXuatBan() > currentYear + 1) {
                throw new BadRequestException("Năm xuất bản tài liệu không hợp lệ");
            }
        }

        if (id == null && syllabusTaiLieuRepository.existsBySyllabusMonIdAndTen(syllabusMonId, ten)) {
            throw new DuplicateResourceException("Tên tài liệu đã tồn tại trong syllabus môn");
        }

        if (id != null && syllabusTaiLieuRepository.existsBySyllabusMonIdAndTenAndIdNot(syllabusMonId, ten, id)) {
            throw new DuplicateResourceException("Tên tài liệu đã tồn tại trong syllabus môn");
        }
    }

    public void validateDieuKienMonHoc(DieuKienMonHocRequest request, Long id) {
        notNull(request, "Dữ liệu điều kiện môn học không hợp lệ");

        Long syllabusMonId = requireId(request.getSyllabusMonId(), "syllabusMonId");

        trimRequired(request.getLoai(), "Loại điều kiện môn học");
        trimRequired(request.getNoiDung(), "Nội dung điều kiện môn học");

        requireExists(syllabusMonHocRepository, syllabusMonId, "Syllabus môn học");

        positiveOrZeroInt(request.getThuTu(), "Thứ tự điều kiện môn học");

        if (request.getThuTu() != null) {
            if (id == null && dieuKienMonHocRepository.existsBySyllabusMonIdAndThuTu(syllabusMonId, request.getThuTu())) {
                throw new DuplicateResourceException("Thứ tự điều kiện môn học đã tồn tại trong syllabus");
            }

            if (id != null && dieuKienMonHocRepository.existsBySyllabusMonIdAndThuTuAndIdNot(syllabusMonId, request.getThuTu(), id)) {
                throw new DuplicateResourceException("Thứ tự điều kiện môn học đã tồn tại trong syllabus");
            }
        }
    }

    public void validateQuyDoiDiemMau(QuyDoiDiemMauRequest request, Long id) {
        notNull(request, "Dữ liệu quy đổi điểm mẫu không hợp lệ");

        String ma = trimRequired(request.getMa(), "Mã quy đổi điểm mẫu");
        trimRequired(request.getTen(), "Tên quy đổi điểm mẫu");

        validateNguongDiem(request.getNguongTu(), request.getNguongDen(), request.getDiemQuyDoi());

        if (id == null && quyDoiDiemMauRepository.existsByMa(ma)) {
            throw new DuplicateResourceException("Mã quy đổi điểm mẫu đã tồn tại: " + ma);
        }

        if (id != null && quyDoiDiemMauRepository.existsByMaAndIdNot(ma, id)) {
            throw new DuplicateResourceException("Mã quy đổi điểm mẫu đã tồn tại: " + ma);
        }
    }

    public void validateChuongTrinhMonQuyDoiDiemMau(ChuongTrinhMonQuyDoiDiemMauRequest request, Long id) {
        notNull(request, "Dữ liệu gán mẫu quy đổi điểm không hợp lệ");

        Long chuongTrinhMonId = requireId(request.getChuongTrinhMonId(), "chuongTrinhMonId");
        Long quyDoiDiemMauId = requireId(request.getQuyDoiDiemMauId(), "quyDoiDiemMauId");

        requireExists(chuongTrinhMonRepository, chuongTrinhMonId, "Môn trong chương trình");
        requireExists(quyDoiDiemMauRepository, quyDoiDiemMauId, "Quy đổi điểm mẫu");

        if (id == null && chuongTrinhMonQuyDoiDiemMauRepository.existsByChuongTrinhMonIdAndQuyDoiDiemMauId(chuongTrinhMonId, quyDoiDiemMauId)) {
            throw new DuplicateResourceException("Mẫu quy đổi điểm đã được gán cho môn này");
        }

        if (id != null && chuongTrinhMonQuyDoiDiemMauRepository.existsByChuongTrinhMonIdAndQuyDoiDiemMauIdAndIdNot(chuongTrinhMonId, quyDoiDiemMauId, id)) {
            throw new DuplicateResourceException("Mẫu quy đổi điểm đã được gán cho môn này");
        }
    }

    public void validateMucTieuChuongTrinh(MucTieuChuongTrinhRequest request, Long id) {
        notNull(request, "Dữ liệu mục tiêu chương trình không hợp lệ");

        Long versionId = requireId(request.getChuongTrinhVersionId(), "chuongTrinhVersionId");

        trimRequired(request.getLoai(), "Loại mục tiêu");
        trimRequired(request.getNoiDung(), "Nội dung mục tiêu");

        requireExists(chuongTrinhVersionRepository, versionId, "Phiên bản chương trình");
        validateThuTuTheoVersion(mucTieuChuongTrinhRepository, versionId, request.getThuTu(), id, "mục tiêu chương trình");
    }

    public void validateNangLucDauRa(NangLucDauRaRequest request, Long id) {
        notNull(request, "Dữ liệu năng lực đầu ra không hợp lệ");

        Long versionId = requireId(request.getChuongTrinhVersionId(), "chuongTrinhVersionId");
        String ma = trimRequired(request.getMa(), "Mã năng lực đầu ra");

        trimRequired(request.getLoai(), "Loại năng lực đầu ra");
        trimRequired(request.getNoiDung(), "Nội dung năng lực đầu ra");

        requireExists(chuongTrinhVersionRepository, versionId, "Phiên bản chương trình");

        if (id == null && nangLucDauRaRepository.existsByChuongTrinhVersionIdAndMa(versionId, ma)) {
            throw new DuplicateResourceException("Mã năng lực đầu ra đã tồn tại trong version: " + ma);
        }

        if (id != null && nangLucDauRaRepository.existsByChuongTrinhVersionIdAndMaAndIdNot(versionId, ma, id)) {
            throw new DuplicateResourceException("Mã năng lực đầu ra đã tồn tại trong version: " + ma);
        }

        validateThuTuTheoVersion(nangLucDauRaRepository, versionId, request.getThuTu(), id, "năng lực đầu ra");
    }

    public void validateViTriViecLam(ViTriViecLamRequest request, Long id) {
        notNull(request, "Dữ liệu vị trí việc làm không hợp lệ");

        Long versionId = requireId(request.getChuongTrinhVersionId(), "chuongTrinhVersionId");

        trimRequired(request.getTen(), "Tên vị trí việc làm");
        requireExists(chuongTrinhVersionRepository, versionId, "Phiên bản chương trình");

        validateThuTuTheoVersion(viTriViecLamRepository, versionId, request.getThuTu(), id, "vị trí việc làm");
    }

    public void validateDieuKienTotNghiep(DieuKienTotNghiepRequest request, Long id) {
        notNull(request, "Dữ liệu điều kiện tốt nghiệp không hợp lệ");

        Long versionId = requireId(request.getChuongTrinhVersionId(), "chuongTrinhVersionId");

        trimRequired(request.getNoiDung(), "Nội dung điều kiện tốt nghiệp");
        requireExists(chuongTrinhVersionRepository, versionId, "Phiên bản chương trình");

        validateThuTuTheoVersion(dieuKienTotNghiepRepository, versionId, request.getThuTu(), id, "điều kiện tốt nghiệp");
    }

    public void validateMucTieuChuongTrinhGoc(MucTieuChuongTrinhGocRequest request, Long id) {
        notNull(request, "Dữ liệu mục tiêu gốc không hợp lệ");

        String ma = trimRequired(request.getMa(), "Mã mục tiêu gốc");

        if (request.getLoai() == null) {
            throw new BadRequestException("Loại mục tiêu gốc không được để trống");
        }

        trimRequired(request.getNoiDung(), "Nội dung mục tiêu gốc");
        checkMaGoc(mucTieuChuongTrinhGocRepository, ma, id, "Mã mục tiêu gốc");
    }

    public void validateNangLucDauRaGoc(NangLucDauRaGocRequest request, Long id) {
        notNull(request, "Dữ liệu năng lực gốc không hợp lệ");

        String ma = trimRequired(request.getMa(), "Mã năng lực gốc");

        if (request.getLoai() == null) {
            throw new BadRequestException("Loại năng lực gốc không được để trống");
        }

        trimRequired(request.getNoiDung(), "Nội dung năng lực gốc");
        checkMaGoc(nangLucDauRaGocRepository, ma, id, "Mã năng lực gốc");
    }

    public void validateViTriViecLamGoc(ViTriViecLamGocRequest request, Long id) {
        notNull(request, "Dữ liệu vị trí việc làm gốc không hợp lệ");

        String ma = trimRequired(request.getMa(), "Mã vị trí việc làm gốc");

        trimRequired(request.getTen(), "Tên vị trí việc làm gốc");
        checkMaGoc(viTriViecLamGocRepository, ma, id, "Mã vị trí việc làm gốc");
    }

    public void validateDieuKienTotNghiepGoc(DieuKienTotNghiepGocRequest request, Long id) {
        notNull(request, "Dữ liệu điều kiện tốt nghiệp gốc không hợp lệ");

        String ma = trimRequired(request.getMa(), "Mã điều kiện tốt nghiệp gốc");

        trimRequired(request.getNoiDung(), "Nội dung điều kiện tốt nghiệp gốc");
        checkMaGoc(dieuKienTotNghiepGocRepository, ma, id, "Mã điều kiện tốt nghiệp gốc");
    }

    public void validateChuongTrinhVersionMucTieu(ChuongTrinhVersionMucTieuRequest request, Long id) {
        Long versionId = requireId(request.getChuongTrinhVersionId(), "chuongTrinhVersionId");
        Long gocId = requireId(request.getMucTieuGocId(), "mucTieuGocId");

        requireExists(chuongTrinhVersionRepository, versionId, "Phiên bản chương trình");
        requireExists(mucTieuChuongTrinhGocRepository, gocId, "Mục tiêu gốc");

        positiveOrZeroInt(request.getThuTu(), "Thứ tự mục tiêu gốc trong version");

        if (id == null && chuongTrinhVersionMucTieuRepository.existsByChuongTrinhVersionIdAndMucTieuGocId(versionId, gocId)) {
            throw new DuplicateResourceException("Mục tiêu gốc đã được gán cho version này");
        }

        if (id != null && chuongTrinhVersionMucTieuRepository.existsByChuongTrinhVersionIdAndMucTieuGocIdAndIdNot(versionId, gocId, id)) {
            throw new DuplicateResourceException("Mục tiêu gốc đã được gán cho version này");
        }
    }

    public void validateChuongTrinhVersionNangLuc(ChuongTrinhVersionNangLucRequest request, Long id) {
        Long versionId = requireId(request.getChuongTrinhVersionId(), "chuongTrinhVersionId");
        Long gocId = requireId(request.getNangLucGocId(), "nangLucGocId");

        requireExists(chuongTrinhVersionRepository, versionId, "Phiên bản chương trình");
        requireExists(nangLucDauRaGocRepository, gocId, "Năng lực gốc");

        positiveOrZeroInt(request.getThuTu(), "Thứ tự năng lực gốc trong version");

        if (id == null && chuongTrinhVersionNangLucRepository.existsByChuongTrinhVersionIdAndNangLucGocId(versionId, gocId)) {
            throw new DuplicateResourceException("Năng lực gốc đã được gán cho version này");
        }

        if (id != null && chuongTrinhVersionNangLucRepository.existsByChuongTrinhVersionIdAndNangLucGocIdAndIdNot(versionId, gocId, id)) {
            throw new DuplicateResourceException("Năng lực gốc đã được gán cho version này");
        }
    }

    public void validateChuongTrinhVersionViTriViecLam(ChuongTrinhVersionViTriViecLamRequest request, Long id) {
        Long versionId = requireId(request.getChuongTrinhVersionId(), "chuongTrinhVersionId");
        Long gocId = requireId(request.getViTriGocId(), "viTriGocId");

        requireExists(chuongTrinhVersionRepository, versionId, "Phiên bản chương trình");
        requireExists(viTriViecLamGocRepository, gocId, "Vị trí việc làm gốc");

        positiveOrZeroInt(request.getThuTu(), "Thứ tự vị trí việc làm gốc trong version");

        if (id == null && chuongTrinhVersionViTriViecLamRepository.existsByChuongTrinhVersionIdAndViTriGocId(versionId, gocId)) {
            throw new DuplicateResourceException("Vị trí việc làm gốc đã được gán cho version này");
        }

        if (id != null && chuongTrinhVersionViTriViecLamRepository.existsByChuongTrinhVersionIdAndViTriGocIdAndIdNot(versionId, gocId, id)) {
            throw new DuplicateResourceException("Vị trí việc làm gốc đã được gán cho version này");
        }
    }

    public void validateChuongTrinhVersionDieuKienTotNghiep(ChuongTrinhVersionDieuKienTotNghiepRequest request, Long id) {
        Long versionId = requireId(request.getChuongTrinhVersionId(), "chuongTrinhVersionId");
        Long gocId = requireId(request.getDieuKienGocId(), "dieuKienGocId");

        requireExists(chuongTrinhVersionRepository, versionId, "Phiên bản chương trình");
        requireExists(dieuKienTotNghiepGocRepository, gocId, "Điều kiện tốt nghiệp gốc");

        positiveOrZeroInt(request.getThuTu(), "Thứ tự điều kiện tốt nghiệp gốc trong version");

        if (id == null && chuongTrinhVersionDieuKienTotNghiepRepository.existsByChuongTrinhVersionIdAndDieuKienGocId(versionId, gocId)) {
            throw new DuplicateResourceException("Điều kiện tốt nghiệp gốc đã được gán cho version này");
        }

        if (id != null && chuongTrinhVersionDieuKienTotNghiepRepository.existsByChuongTrinhVersionIdAndDieuKienGocIdAndIdNot(versionId, gocId, id)) {
            throw new DuplicateResourceException("Điều kiện tốt nghiệp gốc đã được gán cho version này");
        }
    }

    private void validateNguongDiem(BigDecimal nguongTu, BigDecimal nguongDen, BigDecimal diemQuyDoi) {
        positiveOrZero(nguongTu, "Ngưỡng từ");
        positiveOrZero(nguongDen, "Ngưỡng đến");

        validateDiemTrongKhoang(diemQuyDoi, "Điểm quy đổi", BigDecimal.ZERO, BigDecimal.TEN);

        if (nguongTu != null && nguongDen != null && nguongTu.compareTo(nguongDen) > 0) {
            throw new BadRequestException("Ngưỡng từ không được lớn hơn ngưỡng đến");
        }
    }

    private void validateDiemTrongKhoang(BigDecimal value, String name, BigDecimal min, BigDecimal max) {
        if (value == null) return;

        if (value.compareTo(min) < 0 || value.compareTo(max) > 0) {
            throw new BadRequestException(name + " phải nằm trong khoảng " + min + " đến " + max);
        }
    }

    private void validateThuTuTheoVersion(Object repository, Long versionId, Integer thuTu, Long id, String tenBang) {
        positiveOrZeroInt(thuTu, "Thứ tự " + tenBang);

        if (thuTu == null) return;

        boolean exists = false;

        if (repository instanceof MucTieuChuongTrinhRepository repo) {
            exists = id == null
                    ? repo.existsByChuongTrinhVersionIdAndThuTu(versionId, thuTu)
                    : repo.existsByChuongTrinhVersionIdAndThuTuAndIdNot(versionId, thuTu, id);
        } else if (repository instanceof NangLucDauRaRepository repo) {
            exists = id == null
                    ? repo.existsByChuongTrinhVersionIdAndThuTu(versionId, thuTu)
                    : repo.existsByChuongTrinhVersionIdAndThuTuAndIdNot(versionId, thuTu, id);
        } else if (repository instanceof ViTriViecLamRepository repo) {
            exists = id == null
                    ? repo.existsByChuongTrinhVersionIdAndThuTu(versionId, thuTu)
                    : repo.existsByChuongTrinhVersionIdAndThuTuAndIdNot(versionId, thuTu, id);
        } else if (repository instanceof DieuKienTotNghiepRepository repo) {
            exists = id == null
                    ? repo.existsByChuongTrinhVersionIdAndThuTu(versionId, thuTu)
                    : repo.existsByChuongTrinhVersionIdAndThuTuAndIdNot(versionId, thuTu, id);
        }

        if (exists) {
            throw new DuplicateResourceException("Thứ tự " + tenBang + " đã tồn tại trong version: " + thuTu);
        }
    }

    private void checkMaGoc(Object repository, String ma, Long id, String label) {
        boolean exists = false;

        if (repository instanceof MucTieuChuongTrinhGocRepository repo) {
            exists = id == null ? repo.existsByMa(ma) : repo.existsByMaAndIdNot(ma, id);
        } else if (repository instanceof NangLucDauRaGocRepository repo) {
            exists = id == null ? repo.existsByMa(ma) : repo.existsByMaAndIdNot(ma, id);
        } else if (repository instanceof ViTriViecLamGocRepository repo) {
            exists = id == null ? repo.existsByMa(ma) : repo.existsByMaAndIdNot(ma, id);
        } else if (repository instanceof DieuKienTotNghiepGocRepository repo) {
            exists = id == null ? repo.existsByMa(ma) : repo.existsByMaAndIdNot(ma, id);
        }

        if (exists) {
            throw new DuplicateResourceException(label + " đã tồn tại: " + ma);
        }
    }

    private <T, ID> void requireExists(JpaRepository<T, ID> repository, ID id, String label) {
        if (id == null) {
            throw new BadRequestException(label + " không được để trống");
        }

        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException(label + " không tồn tại: " + id);
        }
    }

    private Long requireId(Long id, String field) {
        if (id == null) {
            throw new BadRequestException(field + " không được để trống");
        }

        return id;
    }

    private void notNull(Object value, String message) {
        if (value == null) {
            throw new BadRequestException(message);
        }
    }

    private String trimRequired(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new BadRequestException(fieldName + " không được để trống");
        }

        return value.trim();
    }

    private void positiveOrZero(BigDecimal value, String fieldName) {
        if (value != null && value.compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException(fieldName + " không được âm");
        }
    }

    private void positiveOrZeroInt(Integer value, String fieldName) {
        if (value != null && value < 0) {
            throw new BadRequestException(fieldName + " không được âm");
        }
    }

    private void checkTotalEqualsParts(BigDecimal total, BigDecimal lyThuyet, BigDecimal thucHanh, BigDecimal kiemTra, String label) {
        if (total == null || lyThuyet == null || thucHanh == null || kiemTra == null) return;

        BigDecimal sum = lyThuyet.add(thucHanh).add(kiemTra);

        if (total.compareTo(sum) != 0) {
            throw new BadRequestException(label + " phải bằng giờ lý thuyết + giờ thực hành + giờ kiểm tra");
        }
    }
}
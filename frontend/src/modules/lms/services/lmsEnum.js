// ===== ENUM CONSTANTS LMS DUNG THEO DB/BE MOI =====

export const LOAI_CAU_HOI = {
    trac_nghiem_1_dap_an: 'Trắc nghiệm 1 đáp án',
    trac_nghiem_nhieu_dap_an: 'Trắc nghiệm nhiều đáp án',
    tu_luan: 'Tự luận',
    dung_sai: 'Đúng/Sai',
    dien_khuyet: 'Điền khuyết',
}

export const MUC_DO = {
    de: 'Dễ',
    trung_binh: 'Trung bình',
    kho: 'Khó',
}

export const TRANG_THAI_CAU_HOI_mau = {
    dang_su_dung: 'Đang sử dụng',
    ngung_su_dung: 'Ngưng sử dụng',
    huy: 'Hủy',
}

export const TRANG_THAI_CAU_HOI_VERSION = {
    nhap: 'Nháp',
    cho_duyet: 'Chờ duyệt',
    dang_su_dung: 'Đang sử dụng',
    can_ra_soat: 'Cần rà soát',
    ngung_su_dung: 'Ngưng sử dụng',
    bi_loai: 'Bị loại',
}

export const TRANG_THAI_CAU_HOI_AP_DUNG = {
    can_ra_soat: 'Cần rà soát',
    ap_dung: 'Áp dụng',
    khong_ap_dung: 'Không áp dụng',
    ngung_su_dung: 'Ngưng sử dụng',
}

export const TRANG_THAI_CAU_HOI = TRANG_THAI_CAU_HOI_VERSION

export const LOAI_BAI = {
    bai_tap_ve_nha: 'Bài tập về nhà',
    quiz: 'Quiz',
    kiem_tra: 'Kiểm tra',
    on_tap: 'Ôn tập',
    thi_thu: 'Thi thử',
}

export const TRANG_THAI_BAI_TAP_ONLINE = {
    nhap: 'Nháp',
    san_sang: 'Sẵn sàng',
    an: 'Ẩn',
    huy: 'Hủy',
}

export const TRANG_THAI_BAI_TAP_LOP = {
    chua_mo: 'Chưa mở',
    dang_mo: 'Đang mở',
    tam_dong: 'Tạm đóng',
    da_dong: 'Đã đóng',
    da_chot: 'Đã chốt',
    da_day_sang_diem: 'Đã đẩy sang điểm',
    huy: 'Hủy',
}

export const TRANG_THAI_LAN_LAM_BAI = {
    dang_lam: 'Đang làm',
    da_nop: 'Đã nộp',
    qua_han: 'Quá hạn',
    chua_cham: 'Chưa chấm',
    da_cham: 'Đã chấm',
    huy: 'Hủy',
    bi_reset: 'Bị reset',
}

export const CACH_TINH_DIEM = {
    lay_diem_cao_nhat: 'Lấy điểm cao nhất',
    lay_diem_lan_cuoi: 'Lấy điểm lần cuối',
    lay_diem_trung_binh: 'Lấy điểm trung bình',
    giao_vien_chon: 'Giáo viên chọn',
}

export const TRANG_THAI_KET_QUA = {
    tam_tinh: 'Tạm tính',
    da_chot: 'Đã chốt',
    da_day_sang_diem: 'Đã đẩy sang điểm',
    huy: 'Hủy',
}

export const KET_QUA_DAT = {
    dat: 'Đạt',
    khong_dat: 'Không đạt',
}

export const TRANG_THAI_MA_TRAN_DE = {
    nhap: 'Nháp',
    dang_su_dung: 'Đang sử dụng',
    ngung_su_dung: 'Ngưng sử dụng',
    huy: 'Hủy',
}

export const TRANG_THAI_BAI_NOP_LMS = {
    nhap: 'Nháp',
    da_nop: 'Đã nộp',
    nop_muon: 'Nộp muộn',
    da_cham: 'Đã chấm',
    tra_lai: 'Trả lại',
    huy: 'Hủy',
}

export const PHAM_VI_GIAO_BAI = {
    theo_lop: 'Theo lớp',
    theo_nganh: 'Theo ngành',
    theo_chuong_trinh: 'Theo chương trình',
}

export const NGUON_TAO = {
    giao_vien: 'Giáo viên',
    phong_dao_tao: 'Phòng đào tạo',
    admin: 'Admin',
}

export function labelOf(map, value) {
    return map?.[value] || value || '—'
}

export function badgeClass(value) {
    const map = {
        nhap: 'badge-gray',
        cho_duyet: 'badge-orange',
        dang_su_dung: 'badge-green',
        san_sang: 'badge-green',
        ap_dung: 'badge-green',
        can_ra_soat: 'badge-orange',
        khong_ap_dung: 'badge-red',
        ngung_su_dung: 'badge-gray',
        an: 'badge-gray',
        huy: 'badge-red',
        bi_loai: 'badge-red',
        chua_mo: 'badge-gray',
        dang_mo: 'badge-green',
        tam_dong: 'badge-orange',
        da_dong: 'badge-blue',
        da_chot: 'badge-indigo',
        da_day_sang_diem: 'badge-purple',
        dang_lam: 'badge-blue',
        da_nop: 'badge-green',
        qua_han: 'badge-red',
        chua_cham: 'badge-orange',
        da_cham: 'badge-indigo',
        tam_tinh: 'badge-orange',
        dat: 'badge-green',
        khong_dat: 'badge-red',
    }
    return map[value] || 'badge-gray'
}

export const mauTrangThaiBaiTapLop = badgeClass
export const mauTrangThaiCauHoi = badgeClass
export const mauTrangThaiBaiTapOnline = badgeClass
export const mauTrangThaiLanLam = badgeClass
export const mauKetQuaDat = badgeClass

export function mauMucDo(md) {
    return { de: 'badge-green', trung_binh: 'badge-blue', kho: 'badge-orange' }[md] || 'badge-gray'
}

export function formatDateTime(dt) {
    if (!dt) return '—'
    const d = new Date(dt)
    if (Number.isNaN(d.getTime())) return dt
    return d.toLocaleDateString('vi-VN') + ' ' + d.toLocaleTimeString('vi-VN', { hour: '2-digit', minute: '2-digit' })
}

export function formatDate(dt) {
    if (!dt) return '—'
    const d = new Date(dt)
    if (Number.isNaN(d.getTime())) return dt
    return d.toLocaleDateString('vi-VN')
}

export function formatDiem(diem) {
    if (diem === null || diem === undefined || diem === '') return '—'
    const n = Number(diem)
    if (Number.isNaN(n)) return diem
    return n.toFixed(1)
}
